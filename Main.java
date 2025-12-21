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
    // profitData[ayIndex][gunIndex][emtiaIndex] = kâr
    // ayIndex: 0..11, gunIndex: 0..27 (1. gün => index 0), emtiaIndex: 0..4
    static int[][][] profitData = new int[MONTHS][DAYS][COMMS];

    // yardımcı metotlar

    private static boolean isValidMonth(int month) {
        return month >= 0 && month < MONTHS;
    }

    private static boolean isValidDay(int day) {
        return day >= 1 && day <= DAYS;
    }

    /** emtia indexini (0..4) döndürür. bulunamazsa ya da geçersizse -1 döndürür. büyük küçük harf duyarlı */
    private static int getCommodityIndex(String commodity) {
        if (commodity == null) return -1;
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(commodity)) return i;
        }
        return -1;
    }

    /** dosya yolu oluştur zorunlu klasör adı "Data_Files" olmalı */
    private static Path monthFilePath(int monthIndex) {
        // Zorunlu yapı: Data_Files/January.txt ... Data_Files/December.txt
        return Paths.get("Data_Files", months[monthIndex] + ".txt");
    }

    /** güvenli integer parse; hata olursa null döndürür (asla exception fırlatmaz). */
    private static Integer tryParseInt(String s) {
        if (s == null) return null;
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    public static void loadData() {
        // diziyi 0 la sıfırla (loadData() birden fazla kez çagrılsa da sağlam çalışsın)
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                for (int c = 0; c < COMMS; c++) {
                    profitData[m][d][c] = 0;
                }
            }
        }

        // her ay dosyasını oku ve profitData dizisini doldur
        for (int m = 0; m < MONTHS; m++) {
            Scanner reader = null;
            try {
                reader = new Scanner(monthFilePath(m));
                while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    if (line == null) continue;
                    line = line.trim();
                    if (line.length() == 0) continue;

                    // varsa başlıgi atla: Day,Commodity,Profit
                    if (line.startsWith("Day")) continue;

                    // beklenen format: Day,Commodity,Profit
                    String[] parts = line.split(",");
                    if (parts.length != 3) continue;

                    Integer day = tryParseInt(parts[0]);
                    String comm = parts[1] == null ? null : parts[1].trim();
                    Integer profit = tryParseInt(parts[2]);

                    if (day == null || profit == null) continue;
                    if (!isValidDay(day)) continue;
                    int ci = getCommodityIndex(comm);
                    if (ci == -1) continue;

                    profitData[m][day - 1][ci] = profit;
                }
            } catch (Exception e) {
                // asla exception fırlatma / ekrana yazdırma
                // dosya yoksa ya da okunamazsa o ay için değerler 0 kalır
            } finally {
                if (reader != null) {
                    try { reader.close(); } catch (Exception ignored) {}
                }
            }
        }
    }

    // ======== 10 REQUIRED METHODS (Students fill these) ========

    // ay: 0=January 11=December
    // dönüş: "Commodity totalProfit"  | geçersiz ay: "INVALID_MONTH"
    public static String mostProfitableCommodityInMonth(int month) {
        if (!isValidMonth(month)) return "INVALID_MONTH";

        int bestIdx = 0;
        int bestSum = Integer.MIN_VALUE;

        for (int c = 0; c < COMMS; c++) {
            int sum = 0;
            for (int d = 0; d < DAYS; d++) {
                sum += profitData[month][d][c];
            }
            if (sum > bestSum) {
                bestSum = sum;
                bestIdx = c;
            }
        }
        return commodities[bestIdx] + " " + bestSum;
    }

    // dönuş: o gün tüm emtiaların toplam karı
    // geçersiz ay veya gün: -99999
    public static int totalProfitOnDay(int month, int day) {
        if (!isValidMonth(month) || !isValidDay(day)) return -99999;
        int total = 0;
        int di = day - 1;
        for (int c = 0; c < COMMS; c++) {
            total += profitData[month][di][c];
        }
        return total;
    }


    //  dönüş: emtianın tüm aylar boyunca verilen gün aralığındaki toplam kârı
    // geçersiz emtia, aralık, veya from > to: -99999
    public static int commodityProfitInRange(String commodity, int from, int to) {
        int ci = getCommodityIndex(commodity);
        if (ci == -1) return -99999;
        if (!isValidDay(from) || !isValidDay(to)) return -99999;
        if (from > to) return -99999;

        int sum = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int day = from; day <= to; day++) {
                sum += profitData[m][day - 1][ci];
            }
        }
        return sum;
    }

    // dönüş: o ay içindeki en yüksek toplam karın olduğu gün numarası (1-28)
    // geçersiz ay: -1
    public static int bestDayOfMonth(int month) {
        if (!isValidMonth(month)) return -1;

        int bestDay = 1;
        int bestTotal = Integer.MIN_VALUE;
        for (int day = 1; day <= DAYS; day++) {
            int total = 0;
            int di = day - 1;
            for (int c = 0; c < COMMS; c++) {
                total += profitData[month][di][c];
            }
            if (total > bestTotal) {
                bestTotal = total;
                bestDay = day;
            }
        }
        return bestDay;
    }


    // dönüş: bu emtia için toplam kârı en yüksek olan ayın adı
    // geçersiz emtia: "INVALID_COMMODITY"
    public static String bestMonthForCommodity(String comm) {
        int ci = getCommodityIndex(comm);
        if (ci == -1) return "INVALID_COMMODITY";

        int bestMonth = 0;
        int bestSum = Integer.MIN_VALUE;
        for (int m = 0; m < MONTHS; m++) {
            int sum = 0;
            for (int d = 0; d < DAYS; d++) {
                sum += profitData[m][d][ci];
            }
            if (sum > bestSum) {
                bestSum = sum;
                bestMonth = m;
            }
        }
        return months[bestMonth];
    }

    // donus: yıl boyunca (tüm aylar) ardışık negatif kâr günlerinin en uzun serisi
    // geçersiz emtia: -1
    public static int consecutiveLossDays(String comm) {
        int ci = getCommodityIndex(comm);
        if (ci == -1) return -1;

        int best = 0;
        int current = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (profitData[m][d][ci] < 0) {
                    current++;
                    if (current > best) best = current;
                } else {
                    current = 0;
                }
            }
        }
        return best;
    }

    // dönüş: yıl boyunca karı threshold (rşik) değerinden büyük olan gün sayısı
    // gecersiz emtia: -1
    public static int daysAboveThreshold(String comm, int threshold) {
        int ci = getCommodityIndex(comm);
        if (ci == -1) return -1;

        int count = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (profitData[m][d][ci] > threshold) count++;
            }
        }
        return count;
    }

    // dönüş: bir ay içinde ardışık iki günün toplam karı arasındaki en büyük mutlak fark
    // geçersiz ay: -99999
    public static int biggestDailySwing(int month) {
        if (!isValidMonth(month)) return -99999;

        int bestSwing = 0;

        // 1. günün toplam kârı
        int prevTotal = 0;
        for (int c = 0; c < COMMS; c++) {
            prevTotal += profitData[month][0][c];
        }

        // 2..28. günleri bir önceki günle kıyasla
        for (int d = 1; d < DAYS; d++) {
            int currTotal = 0;
            for (int c = 0; c < COMMS; c++) {
                currTotal += profitData[month][d][c];
            }
            int swing = Math.abs(currTotal - prevTotal);
            if (swing > bestSwing) bestSwing = swing;
            prevTotal = currTotal;
        }
        return bestSwing;
    }

    // dönüş: "C1 is better by X" veya "C2 is better by X" veya "Equal"
    // geçersiz emtia: "INVALID_COMMODITY"
    public static String compareTwoCommodities(String c1, String c2) {
        int i1 = getCommodityIndex(c1);
        int i2 = getCommodityIndex(c2);
        if (i1 == -1 || i2 == -1) return "INVALID_COMMODITY";

        int s1 = 0;
        int s2 = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                s1 += profitData[m][d][i1];
                s2 += profitData[m][d][i2];
            }
        }

        if (s1 == s2) return "Equal";
        if (s1 > s2) return c1 + " is better by " + (s1 - s2);
        return c2 + " is better by " + (s2 - s1);
    }


    // hafta 1: gün 1-7, hafta 2: 8-14, hafta 3: 15-21, hafta 4: 22-28
    // dönüş: toplam karı en yüksek olan haftayı "Week k" formatında döndürür
    // geçersiz ay: "INVALID_MONTH"
    public static String bestWeekOfMonth(int month) {
        if (!isValidMonth(month)) return "INVALID_MONTH";

        int bestWeek = 1;
        int bestSum = Integer.MIN_VALUE;

        for (int w = 1; w <= 4; w++) {
            int startDay = (w - 1) * 7 + 1;
            int endDay = w * 7;

            int sum = 0;
            for (int day = startDay; day <= endDay; day++) {
                int di = day - 1;
                for (int c = 0; c < COMMS; c++) {
                    sum += profitData[month][di][c];
                }
            }

            if (sum > bestSum) {
                bestSum = sum;
                bestWeek = w;
            }
        }
        return "Week " + bestWeek;
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded - ready for queries");
    }
}