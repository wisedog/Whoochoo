/**
 * 
 */
package net.wisedog.android.whooing.sms;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author newmoni
 *
 */
public final class je
{
  int a;
  String b;
  String c;
  String d;
  String e;
  String f;
  String g = "";
  String h = "";
  String i = "";
  String j = "";
  String k = "";
  String[] l = { "USD", "EUR", "JPY", "IDR", "GBP", "CHF", "CAD", "AUD", "NZD", "HKD", "SEK", "DKK", "NOK", "SAR", "KWD", "BHD", "AED", "THB", "SGD", "MYR", "CNY", "BRL", "VND", "TRY", "MXN", "BOB", "TWD", "PHP", "CZK", "INR", "MOP", "QAR", "EGP" };

  public je()
  {
    this.a = 0;
    this.b = "02114";
    this.c = "";
    this.d = "2011";
    this.e = "";
    this.f = "";
  }

  public je(String paramString1, String paramString2, String paramString3)
  {
    this.a = 0;
    this.b = paramString1;
    this.c = paramString2;
    this.d = paramString3;
    this.e = null;
  }

  public je(String paramString1, String paramString2, String paramString3, byte paramByte)
  {
    this.b = paramString1;
    this.c = paramString2;
    this.d = paramString3;
    this.e = "";
  }

  private static boolean b(String paramString)
  {
    int m = 0;
    String[] arrayOfString = { "신한#", "신한은행#", "신한금융투자#", "KB#", "하나,#", "하나은행#", "우리#", "우리투자#", "우리은행#", "광주#", "Citi#", "동양#", "농협#", "신협#", "수협#", "KEB#", "KDB,#", "(KDB)#", "경남#", "한성#", "기업은행#", "부산#", "전북#", "전북은행#", "한화투자#", "한화증권#", "메리츠CMA#", "현대증권CMA", "제일#", "SC#", "SC은행#", "늘푸른은행#", "우체국,#", "우체국#", "증권#", "한국투자#", "미래에셋#", "신협#", "금고#", "대구은행#", "기타매체#", "대체출금#", "인터넷출금#", "인터넷입금#", "자동이체#", "스마트출금#", "ＣＤ현찰#", "ＣＤ공동#", "에스앤저축#" };
    boolean bool = false;
    while (true)
    {
      if (m >= arrayOfString.length)
        return bool;
      if (paramString.contains(arrayOfString[m]))
      {
        m = arrayOfString.length;
        bool = true;
      }
      m++;
    }
  }

  private boolean c(String paramString)
  {
    int m = 0;
    boolean bool = false;
    while (true)
    {
      if (m >= this.l.length)
        return bool;
      if (paramString.contains(this.l[m]))
      {
        bool = true;
        m = this.l.length;
      }
      m++;
    }
  }

/*  private static boolean d(String paramString)
  {
    String[][] arrayOfString = { { "KB", "(US$)" }, { "신한카드해외", "승인", "취소" }, { "신한", "(US)", "(GB)", "(JP)", "(ES)", "(SG)", "(HK)", "(BR)", "(PH)", "(CA)", "(FR)", "(BO)", "(IE)", "(LU)", "(MO)", "(VN)" } };
    int m = 0;
    boolean bool1 = false;
    if (m >= arrayOfString;.length)
      return bool1;
    boolean bool2;
    if (paramString.contains(arrayOfString;[m][0]))
      bool2 = bool1;
    for (int n = 1; ; n++)
    {
      if (n >= arrayOfString[m].length)
      {
        m = arrayOfString.length;
        bool1 = bool2;
        m++;
        break;
      }
      if (paramString.contains(arrayOfString;[m][n]))
      {
        n = arrayOfString[m].length;
        bool2 = true;
      }
    }
  }

  public final void a()
  {
    this.c = this.c.replaceFirst("@유현유성연송사랑해@", "#");
    this.c = this.c.replaceFirst("ㄴㅁ이@런ㅇ@ㄹ", "어머님");
    this.c = this.c.replaceFirst("ㄴㄴㅇㄹㄴ@ㅇㄴ@ㅇㄹㅁ이@런ㅇ@ㄹ", "어머님");
    do
      this.c = this.c.replaceAll("##", "#");
    while (this.c.contains("##"));
  }

  public final void a(String paramString)
  {
    String str;
    label89: label124: int m;
    label236: Matcher localMatcher2;
    label324: Matcher localMatcher3;
    if (paramString.length() > 12)
    {
      str = paramString.substring(0, 12);
      if (str.contains("#"))
        str = str.split("#")[0] + "#";
      if (((!paramString.contains("취소")) || (b(str))) && (!paramString.contains("승인취")))
        break label408;
      this.g = "NP";
      this.f = this.g;
      if ((!paramString.contains("체크")) && (!paramString.contains("잔액")))
        break label491;
      this.h = "CHK";
      this.f = (this.h + "#X#" + this.f);
      this.j = "NOR";
      this.f = ("NOR#" + this.f);
      if ((!paramString.contains("개월")) && ((!paramString.contains("할부")) || (paramString.contains("교원할부")) || (paramString.contains("할부금융"))))
        break label501;
      this.i = "DIV";
      this.f = (this.i + "#" + this.f);
      if ((paramString.contains("KRW")) || ((!d(paramString)) && (!c(paramString))))
        break label670;
      m = 0;
      if (m < this.l.length)
        break label511;
      this.k = "USD";
      localMatcher2 = Pattern.compile("[ ][.][0-9][0-9][ ]").matcher(paramString);
      if (localMatcher2.find())
        break label582;
      localMatcher3 = Pattern.compile("[0-9][.][0-9][0-9]").matcher(paramString);
      label344: if (localMatcher3.find())
        break label618;
    }
    while (true)
    {
      this.f = ("KAD#114#" + this.k + "#" + this.f);
      this.c = paramString;
      return;
      str = paramString.substring(0, paramString.length());
      break;
      label408: if (b(str))
      {
        if ((paramString.contains("입금")) || (paramString.contains("취소")))
        {
          this.g = "AP";
          if ((!paramString.contains("출금")) || (!paramString.contains("(#입금#)")))
            break label89;
          this.g = "NP";
          break label89;
        }
        this.g = "NP";
        break label89;
      }
      this.g = "AP";
      break label89;
      label491: this.h = "CAR";
      break label124;
      label501: this.i = "ONE";
      break label236;
      label511: Matcher localMatcher1 = Pattern.compile(this.l[m]).matcher(paramString);
      while (true)
      {
        if (!localMatcher1.find())
        {
          m++;
          break;
        }
        paramString = paramString.replaceAll(this.l[m], "#" + this.l[m] + "#");
      }
      label582: paramString = paramString.replaceAll("[ ][.][0-9][0-9][ ]", "#0" + localMatcher2.group().substring(1));
      break label324;
      label618: paramString = paramString.replaceAll("[0-9][.][0-9][0-9]", localMatcher3.group().substring(0, 1) + "@!@!@" + localMatcher3.group().substring(2));
      break label344;
      label670: this.k = "KOR";
    }
  }

  public final String toString()
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    return "[" + localSimpleDateFormat.format(Long.valueOf(this.d)) + "] " + this.b + " \n" + this.c;
  }*/
}