package ir.rainyday.android.common.helpers;

import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;

import kotlin.text.Regex;


public final class StringHelper {

    @NotNull
    public static String thousandSeparator(long number) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(number);
    }

    @NotNull
    public static String thousandSeparator(int number) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(number);
    }

    @Nullable
    public static Object transformToPersian(@NotNull Object input, @NotNull Class<Object> type) {
        String str = transformToPersian(input.toString());
        return ConvertorUtil.INSTANCE.convert(str, type);
    }

    @NotNull
    public static String transformToPersian(@NotNull String input) {
        char[] chars = new char[]{'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        int i = 0;

        for (int var5 = input.length(); i < var5; ++i) {
            if (Character.isDigit(input.charAt(i))) {
                builder.append(chars[input.charAt(i) - 48]);
                String var6 = "char - " + input.charAt(i) + " " + (input.charAt(i) - 48) + " - " + chars[input.charAt(i) - 48];
                System.out.println(var6);
            } else {
                builder.append(input.charAt(i));
            }
        }

        return builder.toString();
    }

    @Nullable
    public static Object transformToLatin(@NotNull Object input, @NotNull Class<Object> type) {
        String str = transformToLatin(input.toString());
        return ConvertorUtil.INSTANCE.convert(str, type);
    }

    @NotNull
    public static String transformToLatin(@NotNull String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    @NotNull
    public static String toPascalCase(@NotNull String value) {
        StringBuilder var10000 = new StringBuilder();
        byte var3 = 0;
        byte var4 = 1;
        StringBuilder var5 = var10000;
        String var7 = value.substring(var3, var4);
        String var6 = var7;

        var7 = var6.toUpperCase();
        var6 = var7;
        var10000 = var5.append(var6);
        var3 = 1;
        var5 = var10000;
        var7 = value.substring(var3);
        var6 = var7;
        return var5.append(var6).toString();
    }

    @NotNull
    public static String toCamelCase(@NotNull String value) {
        StringBuilder var10000 = new StringBuilder();
        byte var3 = 0;
        byte var4 = 1;
        StringBuilder var5 = var10000;
        String var7 = value.substring(var3, var4);
        String var6 = var7;

        var7 = var6.toLowerCase();

        var6 = var7;
        var10000 = var5.append(var6);
        var3 = 1;
        var5 = var10000;
        var7 = value.substring(var3);

        var6 = var7;
        return var5.append(var6).toString();

    }

    @NotNull
    public static String toSeparatedString(@Nullable Collection<Object> list, @NotNull String separator, boolean trimEnd) {
        StringBuilder csvBuilder = new StringBuilder();

        for (Object item : list) {
            csvBuilder.append(item.toString());
            csvBuilder.append(separator);
        }

        String csv = csvBuilder.toString();
        System.out.println(csv);

        //Remove last comma
        if (trimEnd)
            csv = csv.substring(0, csv.length() - separator.length());

        return csv;
    }

    @NotNull
    public static String capitalize(@Nullable String value) {
        if (value != null && value.length() != 0) {
            char first = value.charAt(0);
            String var10000;
            if (Character.isUpperCase(first)) {
                var10000 = value;
            } else {
                char var3 = Character.toUpperCase(first);
                byte var5 = 1;
                var10000 = value.substring(var5);
                String var4 = var10000;
                var10000 = var3 + var4;
            }

            return var10000;
        } else {
            return "";
        }
    }



    @NotNull
    public static String trimEnd(@NotNull CharSequence text) {
        String var3 = "\\s++$";
        Regex var5 = new Regex(var3);
        String var4 = "";
        return var5.replaceFirst(text, var4);
    }

    public static boolean isNullOrEmpty(@Nullable CharSequence value) {
        return value == null || TextUtils.isEmpty(value);
    }

    public static boolean isNotNullAndEmpty(@Nullable CharSequence value) {
        return value != null && !TextUtils.isEmpty(value);
    }
}