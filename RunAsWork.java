package vn.com.vng.mcrusprofile.util;

/**
 * @author sonnd
 *
 * @param <Result>
 */
public interface RunAsWork<Result> {
	Result doWork() throws Throwable;
}
