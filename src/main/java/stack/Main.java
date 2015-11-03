package stack;

import java.util.ArrayList;

/**
 * Created by aliakseisemchankau on 20.10.15.
 */
public class Main {

    public static void main(String[] args) {

        Stack<String> st = new ArrayStack<>(4);
        try {
            st.push("a");
            st.push("b");
            st.push("c");
            st.push("d");
        } catch (StackException sExc) {
            System.out.println("something went wrong 1");
        }

        try {
            st.push("e");
        } catch (StackException sExc) {
            System.out.println("something went wrong 2");
        }

        try {
            ArrayList<String> dst = new ArrayList<>();
            st.popAll(dst);
            for (String s : dst) {
                System.out.println(s + " = s");
            }
        } catch (StackException sExc) {
            System.out.println("something went wrong 3");
        }

        try {
            ArrayList<String> src = new ArrayList<>();
            src.add("a");
            src.add("b");
            st.pushAll(src);
            while (!st.isEmpty()) {
                System.out.println(st.pop());
            }
        } catch (StackException sExc) {
            System.out.println("something went wrong 4");
        }

    }

}
