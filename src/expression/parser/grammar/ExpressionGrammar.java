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

    private ArrayList<HashSet<Single>> binaryPriorityLevels;

    public ExpressionGrammar() {
        binaryPriorityLevels = new ArrayList<>();
    }

    public void addLevel() {
        binaryPriorityLevels.add(new HashSet<>());
    }

    public void addLevel(int level) {
        binaryPriorityLevels.add(level, new HashSet<>());
    }

    public void addLast(Single token) {
        assert binaryPriorityLevels.size() != 0 : "Grammar has no priority levels yet";
        if (token.isBinary()) {
            binaryPriorityLevels.get(binaryPriorityLevels.size() - 1).add(token);
        }
    }

    public void addOnLevel(int level, Single token) {
        if (token.isBinary()) {
            while (binaryPriorityLevels.size() < level + 1) {
                binaryPriorityLevels.add(new HashSet<>());
            }
            binaryPriorityLevels.get(level).add(token);
        }
    }

    public boolean isOnLevel(int level, Single token) {
        return binaryPriorityLevels.get(level).contains(token);
    }

    public int size() {
        return binaryPriorityLevels.size();
    }

}
