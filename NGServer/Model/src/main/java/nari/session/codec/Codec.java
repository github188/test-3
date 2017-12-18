package nari.session.codec;

public interface Codec<T> {

	public void encode(ModelFieldWriter writer, T val);

	public Class<T> getEncoderClass();

	public T decode(ModelFieldReader reader);
}
