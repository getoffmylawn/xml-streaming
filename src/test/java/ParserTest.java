import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

interface Hander {
	public void handle(String xml);
}

interface State {
	void handleChar(Context context, int cher);
}

class Context {
	private State state = DefaultState.instance;
	private int tags = 0;
	
	public void handleChar(int cher) {
		state.handleChar(this, cher);
	}

	public void changeState(State state) {
		System.out.println(String.format("From: %s To: %s", this.state.getClass().getSimpleName(), 
				state.getClass().getSimpleName()));
		System.out.println("Tags: " + tags);
		this.state = state;
	}
	
	public void incrementTags() {
		tags++;
	}
	
	public void decrementTags() {
		tags--;
	}
}

class DefaultState implements State {
	public static DefaultState instance = new DefaultState();

	@Override
	public void handleChar(Context context, int cher) {
		switch (cher) {
		case '<' : context.changeState(GTHit.instance);
		}
	}
}

class GTHit implements State {
	public static GTHit instance = new GTHit();

	@Override
	public void handleChar(Context context, int cher) {
		switch (cher) {
		case '/' : context.decrementTags();
		default : context.incrementTags();
		}
        context.changeState(InsideTag.instance);
	}
}

class InsideTag implements State {
	public static InsideTag instance = new InsideTag();

	@Override
	public void handleChar(Context context, int cher) {
		switch (cher) {
		case '/' : context.decrementTags();
		case '>' : context.changeState(DefaultState.instance);
		}
	}
}

class Parser {
	public void parse(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		Context context = new Context();
		int cher;
		while ((cher = reader.read()) != -1) {
//			System.out.println("handling char: " + String.format("%c", cher));
			context.handleChar(cher);
		}
	}
}

public class ParserTest {

	@Test
	public void test() throws IOException {
		String xml = "<foo><bar></bar></foo>";
		InputStream parse = new ByteArrayInputStream(xml.getBytes());
		new Parser().parse(parse);
	}

}
