package nari.session.codec;

public interface CodecRegistry {

	public <T> Codec<T> get(Class<T> typeClass) throws Exception;
}
