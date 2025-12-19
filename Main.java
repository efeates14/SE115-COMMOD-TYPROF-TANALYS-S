// Main.java — Students version
import java.io.*;
import java.util.*;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June",
            "July","August","September","October","November","December"};

    static int[][][] profitData = new int[MONTHS][DAYS][COMMS];

    private static boolean isValidMonth(int month) {
        return month >= 0 && month < MONTHS;
    }

    private static boolean isValidDay(int day) {
        return day >= 1 && day <= DAYS;
    }

    private static int getCommodityIndex(String commodity) {
        if (commodity == null) return -1;
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(commodity)) return i;
        }
        return -1;
    }

    private static String resolveMonthFilePath(int monthIndex) {
        // Primary: Data_Files/January.txt
        String p1 = "Data_Files" + File.separator + months[monthIndex] + ".txt";
        File f1 = new File(p1);
        if (f1.exists() && f1.isFile()) return p1;

        // Fallback: January.txt in current directory
        String p2 = months[monthIndex] + ".txt";
        File f2 = new File(p2);
        if (f2.exists() && f2.isFile()) return p2;

        // Return primary anyway; caller handles errors robustly
        return p1;
    }
    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    public static void loadData() {
        for (int m = 0; m < MONTHS; m++) {
            // dosya yolu
            String filename = "Data_Files/" + months[m] + ".txt";
            File file = new File(filename);

            // dosya yoksa sessizce geç
            if (!file.exists()) {
                continue;
            }

            try {
                Scanner sc = new Scanner(file);

                // başlık satırını (Day,Commodity,Profit) atla
                if (sc.hasNextLine()) {
                    sc.nextLine();
                }

                while (sc.hasNextLine()) {
                    String line = sc.nextLine();

                    // boş satır varsa atla
                    if (line.trim().isEmpty()) continue;

                    // virgül ile ayır
                    String[] parts = line.split(",");

                    if (parts.length >= 3) {
                        try {
                            int day = Integer.parseInt(parts[0].trim());
                            String commodityName = parts[1].trim();
                            int profit = Integer.parseInt(parts[2].trim());

                            int dayIndex = day - 1; // Gün 1 -> Index 0
                            int commIndex = getCommodityIndex(commodityName);

                            // veri sınırlarını kontrol et ve kaydet
                            if (dayIndex >= 0 && dayIndex < DAYS && commIndex != -1) {
                                profitData[m][dayIndex][commIndex] = profit;
                            }
                        } catch (NumberFormatException e) {
                            // hatayı sessizce geç
                        }
                    }
                }
                sc.close();
            } catch (FileNotFoundException e) {
                // sessiz kal
            }
        }
    }
    }

    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {
        return "DUMMY";
    }

    public static int totalProfitOnDay(int month, int day) {
        return 1234;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        return 1234;
    }

    public static int bestDayOfMonth(int month) {
        return 1234;
    }

    public static String bestMonthForCommodity(String comm) {
        return "DUMMY";
    }

    public static int consecutiveLossDays(String comm) {
        return 1234;
    }

    public static int daysAboveThreshold(String comm, int threshold) {
        return 1234;
    }

    public static int biggestDailySwing(int month) {
        return 1234;
    }

    public static String compareTwoCommodities(String c1, String c2) {
        return "DUMMY is better by 1234";
    }

    public static String bestWeekOfMonth(int month) {
        return "DUMMY";
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }
}