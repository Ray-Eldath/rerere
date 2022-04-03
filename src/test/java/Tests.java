import model.match.MatchGraph;
import model.match.format.Formatters;

import java.net.URLEncoder;
import java.nio.charset.Charset;

public class Tests {
    public static void graphviz(MatchGraph graph) {
        var encoded = URLEncoder.encode(graph.format(Formatters.GRAPHVIZ), Charset.defaultCharset()).replace("+", "%20");
        System.out.println("https://dreampuf.github.io/GraphvizOnline/#" + encoded);
    }
}
