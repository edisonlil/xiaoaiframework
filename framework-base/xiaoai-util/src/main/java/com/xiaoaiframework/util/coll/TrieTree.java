package com.xiaoaiframework.util.coll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典数实现
 * @author edison
 */
public class TrieTree {


    TrieNode root = new TrieNode();


    /**
     * 字符节点
     */
    class TrieNode {

        private Map<Character,TrieNode> map;

        /**
         * 所有的儿子节点
         */
        private List<TrieNode> children;


        /**
         * 节点的值
         */
        private char val;


        public TrieNode(char val){
            this();
            this.val = val;
        }

        public TrieNode(){
            map = new HashMap();
            children = new ArrayList<>();
        }


        public TrieNode add(Character c){
            TrieNode node = new TrieNode(c);
            this.children.add(node);
            this.map.put(c,node);
            return node;
        }


        public boolean exist(Character c){
            return this.map.containsKey(c);
        }
    }

    /**
     * 添加词汇
     * @param str
     */
    public void add(String str){

        if(str == null || "".equals(str.trim())){
            return;
        }

        TrieNode self = root;
        char[] letters = str.toCharArray();
        for (char c : letters) {
            if(!self.exist(c)){
               self = self.add(c);  //添加子节点
            }
        }

        return;
    }

    /**
     * 判断字符串是否存在
     * @param str
     * @return
     */
    public boolean exist(String str){

        if(str == null || "".equals(str.trim())){
            return false;
        }

        char[] letters = str.toCharArray();
        TrieNode self = root;
        for (char c : letters) {
            self = self.map.get(c);
            if(self == null){
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串替换
     * @param target
     * @param replacement
     * @return
     */
    public String replace(String target,char replacement){

        StringBuilder builder = new StringBuilder();

        char[] letters = target.toCharArray();
        TrieNode self = root;

        int offset = 0;
        for (int i = 0; i < letters.length; i++) {

            char c = letters[i];

            if(!ignoreMatch(c)){
                self = self.map.get(c);
            }

            if(self != null){
                offset++;
            }else if(self == null && offset == 0){
                builder.append(c);
                self = root;
            } else if(self == null && offset != 0){

                String str = offset == 1 ?  letters[i-offset]+"" : generateChar(replacement,offset);
                builder.append(str);
                self = root;
                i--; //回退到上一个字符从根部开始查找。
                offset = 0;
            }

        }

        String str = offset == 1 ?  letters[letters.length-1]+"" : generateChar(replacement,offset);
        builder.append(str);

        return builder.toString();
    }

    /**
     * 忽略匹配的词。
     * @param c
     * @return
     */
    public boolean ignoreMatch(char c){

        char[] ignores = {' ','.','*',','};

        for (char ignore : ignores) {

            if(ignore == c){
                return true;
            }
        }

        return false;

    }

    /**
     * 生成指定数量的字符序列
     * @param c
     * @param length
     * @return
     */
    public String generateChar(char c,int length){

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(c);
        }

        return builder.toString();
    }


}
