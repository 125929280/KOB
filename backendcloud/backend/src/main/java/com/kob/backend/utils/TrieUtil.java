package com.kob.backend.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;
import org.apache.commons.lang3.CharUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class TrieUtil {
    TrieNode root = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream resourceAsStream = TrieUtil.class.getResourceAsStream("/public/sensitive_words.txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream))
        ) {
            String keyword;
            while ((keyword = bufferedReader.readLine()) != null) {
                this.addKeyWord(keyword);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addKeyWord(String keyword) {
        TrieNode node = root;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode child = node.get(c);
            if (Objects.isNull(child)) {
                child = new TrieNode();
                node.insert(c, child);
            }
            node = child;
            if (i == keyword.length() - 1) {
                node.setEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     *
     * @param text 待过滤文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        TrieNode node = root;
        int begin = 0, end = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (end < text.length()) {
            char c = text.charAt(end);
            if (isSymbol(c)) {
                if (node == root) {
                    stringBuilder.append(c);
                    begin++;
                }
                ++end;
                continue;
            }
            node = node.get(c);
            if (Objects.isNull(node)) {
                stringBuilder.append(text.charAt(begin));
                end = ++begin;
                node = root;
            } else if (node.isEnd()) {
                for (int i = begin; i <= end; i++) {
                    stringBuilder.append("*");
                }
                begin = ++end;
                node = root;
            } else {
                ++end;
            }
        }
        return stringBuilder.append(text.substring(begin)).toString();
    }

    /**
     * 判断是否为符号
     *
     * @param c
     * @return
     */
    private boolean isSymbol(Character c) {
        // 0x2E80 ~ 0x9FFF 是东亚文字
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    @Data
    private static class TrieNode {
        private boolean isEnd;
        private Map<Character, TrieNode> children;

        public TrieNode() {
            isEnd = false;
            children = new HashMap<>();
        }

        public void insert(Character c, TrieNode node) {
            children.put(c, node);
        }

        public TrieNode get(Character c) {
            return children.get(c);
        }
    }

}
