package expression.parser.grammar;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 08:19
 */

public class ExpressionGrammar {

    private ArrayList<HashSet<Token>> priorityLevels;

    public ExpressionGrammar() {
        priorityLevels = new ArrayList<>();
    }

    public void addOnLevel(int level, Token token) {
        if (token.isBinary()) {
            while (priorityLevels.size() < level + 1) {
                priorityLevels.add(new HashSet<>());
            }
            priorityLevels.get(level).add(token);
        }
    }

    public boolean isOnLevel(int level, Token token) {
        return priorityLevels.get(level).contains(token);
    }

    public int size() {
        return priorityLevels.size();
    }

}
