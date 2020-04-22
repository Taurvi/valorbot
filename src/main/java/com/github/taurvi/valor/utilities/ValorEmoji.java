package com.github.taurvi.valor.utilities;

import com.vdurmont.emoji.EmojiParser;

public enum ValorEmoji {
    SWORDS(EmojiParser.parseToUnicode(":crossed_swords:")),
    SHIELD(EmojiParser.parseToUnicode(":shield:")),
    CANCEL(EmojiParser.parseToUnicode(":no_entry_sign:")),
    CHECK(EmojiParser.parseToUnicode(":white_check_mark:"));

    private String emoji;

    ValorEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }
}
