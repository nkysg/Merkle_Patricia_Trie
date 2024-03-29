package com.ysg;

import com.novi.serde.Bytes;
import org.bouncycastle.jcajce.provider.digest.SHA3;

public final class HashValue {

    public static final int LENGTH = 32;
    public static HashValue hashZero;
    public static HashValue hashPlaceholder;
    static {
        byte[] val = new byte[LENGTH];
        hashZero = new HashValue(new Bytes(val));

        byte[] str = "ACCUMULATOR_PLACEHOLDER_HASH".getBytes();
        System.out.println("str" + str.length);
        assert  str.length <= LENGTH;
        byte[] val2 = new byte[LENGTH];
        System.arraycopy(val2, 0, str, 0, str.length);
        hashPlaceholder = new HashValue(new Bytes(val2));
    }

    public final com.novi.serde.Bytes value;

    public HashValue(com.novi.serde.Bytes value) {
        java.util.Objects.requireNonNull(value, "value must not be null");
        this.value = value;
    }

    public static HashValue zero() {

        return hashZero;
    }

    public static HashValue placeholder() {
        return hashPlaceholder;
    }

    public static HashValue sha3Of(byte[] content) {
        byte[] digestedBytes = new SHA3.Digest256().digest(content);
        return new HashValue(com.novi.serde.Bytes.valueOf(digestedBytes));
    }

    public static HashValue deserialize(com.novi.serde.Deserializer deserializer) throws com.novi.serde.DeserializationError {
        deserializer.increase_container_depth();
        Builder builder = new Builder();
        builder.value = deserializer.deserialize_bytes();
        deserializer.decrease_container_depth();
        return builder.build();
    }

    public static HashValue bcsDeserialize(byte[] input) throws com.novi.serde.DeserializationError {
        if (input == null) {
            throw new com.novi.serde.DeserializationError("Cannot deserialize null array");
        }
        com.novi.serde.Deserializer deserializer = new com.novi.bcs.BcsDeserializer(input);
        HashValue value = deserialize(deserializer);
        if (deserializer.get_buffer_offset() < input.length) {
            throw new com.novi.serde.DeserializationError("Some input bytes were not read");
        }
        return value;
    }

    public void serialize(com.novi.serde.Serializer serializer) throws com.novi.serde.SerializationError {
        serializer.increase_container_depth();
        serializer.serialize_bytes(value);
        serializer.decrease_container_depth();
    }

    public byte[] bcsSerialize() throws com.novi.serde.SerializationError {
        com.novi.serde.Serializer serializer = new com.novi.bcs.BcsSerializer();
        serialize(serializer);
        return serializer.get_bytes();
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        HashValue other = (HashValue) obj;
        if (!java.util.Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int value = 7;
        value = 31 * value + (this.value != null ? this.value.hashCode() : 0);
        return value;
    }

    public static final class Builder {
        public com.novi.serde.Bytes value;

        public HashValue build() {
            return new HashValue(
                    value
            );
        }
    }
}