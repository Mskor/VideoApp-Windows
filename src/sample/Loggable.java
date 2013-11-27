package sample;

/**
 * Created to generalize {@link SingletonLogHandle} logger message sources
 * So class should implement {@link Loggable} to be able to use {@link SingletonLogHandle}
 */
public interface Loggable {
	public String GetLogEntry();
}
