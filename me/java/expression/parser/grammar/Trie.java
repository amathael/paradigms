package java.expression.parser.grammar;

import java.util.HashMap;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 29-Mar-18
 * @time 21:44
 */

public class Trie {

    private class Node {
        boolean terminal;
        HashMap<Character, Node> next;

        Node() {
            this.terminal = false;
            next = new HashMap<>();
        }
    }

    private Node root;

    public Trie() {
        root = new Node();
    }

    public void add(String key) {
        Node cur = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!cur.next.containsKey(c)) {
                cur.next.put(c, new Node());
            }
            cur = cur.next.get(c);
        }
        cur.terminal = true;
    }

    public int seek(String input, int index) {
        Node cur = root;
        int i = index;
        boolean isAlpha = true;
        while (i < input.length() && cur.next.containsKey(input.charAt(i))) {
            if (!Character.isAlphabetic(input.charAt(i))) {
                isAlpha = false;
            }
            cur = cur.next.get(input.charAt(i));
            i++;
        }
        return cur.terminal && (i == input.length() || !isAlpha ||
                !Character.isAlphabetic(input.charAt(i)) && !Character.isDigit(input.charAt(i))) ? i : -1;
    }

}
