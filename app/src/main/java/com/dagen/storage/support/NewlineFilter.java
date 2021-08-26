package com.dagen.storage.support;

import android.text.InputFilter;
import android.text.Spanned;

public class NewlineFilter implements InputFilter {

    /**
     * @param source 输入的文字
     * @param start  输入-0，删除-0
     * @param end    输入-文字的长度，删除-0
     * @param dest   原先显示的内容
     * @param dstart 输入-原光标位置，删除-光标删除结束位置
     * @param dend   输入-原光标位置，删除-光标删除开始位置
     * @return null表示原始输入，""表示不接受输入，其他字符串表示变化值
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (source.toString().contains("\n")) {
            return source.toString().replace("\n", "");
        }
        return null;
    }
}
