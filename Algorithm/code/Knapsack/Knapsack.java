package Knapsack;

import java.util.Random;

/**
 * 01背包
 */
public class Knapsack {

    static int Knapsack2D(int capacity, int weight[], int profit[]) {
        int n = weight.length;

        // dp[i][w] 为选前i个物品、背包容量为w时的最大profit
        // n+1 表示包含选0个物品的状态
        // capacity+1 表示包含背包容量为0的状态
        int dp[][] = new int[n + 1][capacity + 1];

        // 从选择前1个物品开始（选择0个物品，profit都为0，不用变），到选择所有物品
        for (int i = 1; i <= n; i++) {
            // 从背包容量为0开始！（可能物品weight为0）
            for (int w = 0; w <= capacity; w++) {
                if (weight[i - 1] <= w) {
                    dp[i][w] = Math.max(profit[i - 1] + dp[i - 1][w - weight[i - 1]], dp[i - 1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        return dp[n][capacity];
    }

    static int Knapsack1D(int capacity, int weight[], int profit[]) {
        int n = weight.length;

        // dp[w]为背包容量为w时的最大profit
        int[] dp = new int[capacity + 1];

        // 从选择前1个物品开始（选择0个物品，profit都为0，不用变），到选择所有物品
        for (int i = 0; i < n; i++) {
            // 从多到少（如果从少到多的话是完全背包，一个物品被多次选择）
            for (int w = capacity; w >= weight[i]; w--) {
                dp[w] = Math.max(dp[w], dp[w - weight[i]] + profit[i]);
            }
        }

        return dp[capacity];
    }

    public static void main(String[] args) {

        int trynums = 1000;
        int itemnums = 1000;
        int capacity = 100;
        int profit[] = new int[itemnums];
        int weight[] = new int[itemnums];

        Random r = new Random();

        for (int j = 0; j < trynums; j++) {
            int seed = r.nextInt();
            System.out.println(seed);
            r.setSeed(seed);
            for (int i = 0; i < itemnums; i++) {
                profit[i] = r.nextInt(capacity * 10);
            }
            for (int i = 0; i < itemnums; i++) {
                weight[i] = r.nextInt(capacity * 10);
            }

            if (Knapsack2D(capacity, weight, profit) != Knapsack1D(capacity, weight, profit)) {
                System.out.println("WRONG!! " + seed);
                System.exit(0);
            }
        }
    }
}
