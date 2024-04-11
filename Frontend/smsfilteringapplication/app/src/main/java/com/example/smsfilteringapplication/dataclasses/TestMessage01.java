package com.example.smsfilteringapplication.dataclasses;

import java.io.IOException;
import java.util.regex.Pattern;

public class TestMessage01 {
    private double[] weight = new double[1];

    public TestMessage01() {
        this.weight[0] = 0.060766536750990724;
    }

    public void Test(String var1) throws IOException {
        double[] var2 = new double[]{analyzeMessage(var1)};
        int var3 = Process(var2, this.weight);
        if (var3 == 1) {
            System.out.println("Result: spam");
        } else {
            System.out.println("Result: ham ");
        }

    }

    private static int Process(double[] var0, double[] var1) {
        double var2 = 0.0;

        for(int var4 = 0; var4 < var0.length; ++var4) {
            var2 += var0[var4] * var1[var4];
        }

        double[] var6 = new double[]{1.0 / (1.0 + Math.exp(-var2))};
        byte var5;
        if (var6[0] >= 0.5478) {
            var5 = 1;
        } else {
            var5 = 0;
        }

        return var5;
    }

    private static double analyzeMessage(String var0) {
        double var1 = 0.0;
        String[] var3 = var0.split(" ");
        String[] var4 = var3;
        int var5 = var3.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            if (!Pattern.compile(Pattern.quote(".com"), 2).matcher(var7).find() && !Pattern.compile(Pattern.quote(".net"), 2).matcher(var7).find() && !Pattern.compile(Pattern.quote(".org"), 2).matcher(var7).find()) {
                if (!Pattern.compile(Pattern.quote(".xyz"), 2).matcher(var7).find() && !Pattern.compile(Pattern.quote(".cn"), 2).matcher(var7).find() && !Pattern.compile(Pattern.quote(".online"), 2).matcher(var7).find() && !Pattern.compile(Pattern.quote(".us"), 2).matcher(var7).find()) {
                    if (Pattern.compile(Pattern.quote(".tk"), 2).matcher(var7).find() || Pattern.compile(Pattern.quote(".buzz"), 2).matcher(var7).find() || Pattern.compile(Pattern.quote(".top"), 2).matcher(var7).find() || Pattern.compile(Pattern.quote(".ga"), 2).matcher(var7).find() || Pattern.compile(Pattern.quote(".ml"), 2).matcher(var7).find() || Pattern.compile(Pattern.quote(".nl"), 2).matcher(var7).find() || Pattern.compile(Pattern.quote(".info"), 2).matcher(var7).find() || Pattern.compile(Pattern.quote(".cf"), 2).matcher(var7).find() || Pattern.compile(Pattern.quote(".gq"), 2).matcher(var7).find() || Pattern.compile(Pattern.quote(".live"), 2).matcher(var7).find() || Pattern.compile(Pattern.quote(".host"), 2).matcher(var7).find()) {
                        var1 += 4.0;
                    }
                } else {
                    var1 += 3.0;
                }
            } else {
                var1 += 2.0;
            }

            if (var7.matches(".*\\d.*")) {
                ++var1;
            }

            if (var7.equalsIgnoreCase("text") || var7.equalsIgnoreCase("free") || var7.equalsIgnoreCase("win") || var7.equalsIgnoreCase("won") || var7.equalsIgnoreCase("call") || var7.equalsIgnoreCase("bonus") || var7.equalsIgnoreCase("prize") || var7.equalsIgnoreCase("cash") || var7.equalsIgnoreCase("claim") || var7.equalsIgnoreCase("reward") || var7.equalsIgnoreCase("refund") || var7.equalsIgnoreCase("winner") || var7.equalsIgnoreCase("txt") || var7.equalsIgnoreCase("congratulations")) {
                var1 += 0.8;
            }

            if (var7.equalsIgnoreCase("click")) {
                ++var1;
                String var8 = var0.substring(var0.toLowerCase().indexOf("click") + 6).trim();
                if (var8.startsWith("here") || var8.startsWith("on") || var8.startsWith("now") || var8.startsWith("&")) {
                    var1 += 3.0;
                }
            }
        }

        return var1;
    }
}