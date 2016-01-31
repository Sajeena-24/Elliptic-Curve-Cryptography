import java.io.*;
 import java.util.*; 
 import java.math.*; 

 class cr1
{
//Finding A and B matching the constraint 4a^3+27b^2!=0
public static void findAB(BigInteger p) {
Random r=new Random();
BigInteger a=BigInteger.ZERO;
BigInteger b=BigInteger.ZERO;
while(a.equals(BigInteger.ZERO) || b.equals(BigInteger.ZERO) || (new BigInteger(4+"").multiply(a.pow(3)).add(new BigInteger(27+"").multiply(b.pow(2)))).equals(BigInteger.ZERO)||a.equals(b)){ a=new BigInteger(r.nextInt((p.subtract(BigInteger.ONE).intValue())-2)+2+""); b=new BigInteger(r.nextInt((p.subtract(BigInteger.ONE).intValue())-2)+2+""); //System.out.println(a+" "+b);
}
System.out.println(a+" "+b); }
//Modular Exponentiation
public static BigInteger modexp(BigInteger a, BigInteger b, BigInteger n) { if (b.equals(BigInteger.ZERO)) return BigInteger.ONE;
BigInteger t1 = modexp(a, b.divide(new BigInteger(2+"")), n); BigInteger c = (t1.multiply(t1)).mod(n);
if (b.mod(new BigInteger(2+"")).equals(BigInteger.ONE))
c = (c.multiply(a)).mod(n); return c;
}
//Finding generator points on the elliptic curve and writing to points.txt file
public static void findPoints(BigInteger p,BigInteger a,BigInteger b) throws Exception
{
LinkedHashSet<String> l1=new LinkedHashSet<String>(); ArrayList<String> a1=new ArrayList<String>(); BigInteger y=BigInteger.ZERO;
BigInteger x=BigInteger.ZERO;
for(long i=0;i<p.longValue();i++)
{
BigInteger temp=new BigInteger(i+""); temp=(temp.pow(3).add(a.multiply(temp)).add(b)).mod(p);
BigInteger y1=modexp(temp,p.add(BigInteger.ONE).divide(new BigInteger(4+"")),p);
BigInteger y2=p.subtract(y1); BigInteger y3=y1.pow(2).mod(p); BigInteger y4=y2.pow(2).mod(p); if(y3.equals(y4) && y3.equals(temp)){ l1.add(i+" "+y1);
//pointonEC(p,new BigInteger(i+""),y1,a);
l1.add(i+" "+y2);
//pointonEC(p,new BigInteger(i+""),y2,a);
}
}
PrintWriter pr1=new PrintWriter("genp.txt");
for(String ff:l1)
pr1.println(ff);
pr1.close();
BufferedReader br=new BufferedReader(new FileReader("genp.txt"));
String line="";
while((line=br.readLine())!=null)
{
StringTokenizer st=new StringTokenizer(line);
a1=pointonEC(p,new BigInteger(st.nextToken()),new BigInteger(st.nextToken()),a); if(a1.size()>=l1.size()-200)
break;
}
PrintWriter pr=new PrintWriter("ans.txt");
//pr.println("Number of points:"+a1.size());
for(String g:a1)
pr.println(g);
pr.println("Number of points:"+a1.size());
pr.close(); }
//Scalar multiplication of generator points till infinity
public static ArrayList<String> pointonEC(BigInteger p,BigInteger x1,BigInteger y1,BigInteger a)
{
ArrayList<String> a1=new ArrayList<String>();
//System.out.println(p+" "+x1.pow(2).multiply(new
BigInteger(3+"")).mod(p).add(a).mod(p).multiply(y1.multiply(new
BigInteger(2+"")).mod(p)));
BigInteger alpha=x1.pow(2).multiply(new BigInteger(3+"")).mod(p).add(a).mod(p).multiply(y1.multiply(new BigInteger(2+"")).mod(p).modInverse(p)).mod(p);
BigInteger x2=alpha.multiply(alpha).subtract(x1.multiply(new BigInteger(2+""))).mod(p);
BigInteger y2=alpha.multiply(x1.subtract(x2)).subtract(y1).mod(p); a1.add(x1+" "+y1);
while(true)
{
BigInteger ch=x2.subtract(x1);
if(ch.equals(new BigInteger(0+"")))
alpha=BigInteger.ZERO;
else
alpha=y2.subtract(y1).mod(p).multiply(ch.modInverse(p)).mod(p); BigInteger x3=alpha.multiply(alpha).subtract(x1).subtract(x2).mod(p);
BigInteger y3=alpha.multiply(x1.subtract(x3)).subtract(y1).mod(p); a1.add("("+x2+","+y2+")");
x2=new BigInteger(x3.toString());
y2=new BigInteger(y3.toString());
if(ch.equals(new BigInteger(0+""))) break;
}
return a1;
}
//Calculating kG where k is a constant and G is a generator point
public static String pointAdd(BigInteger k,BigInteger x1,BigInteger y1,BigInteger a,BigInteger p)
{
BigInteger alpha=x1.pow(2).multiply(new BigInteger(3+"")).mod(p).add(a).mod(p).multiply(y1.multiply(new BigInteger(2+"")).mod(p).modInverse(p)).mod(p);
BigInteger x2=alpha.multiply(alpha).subtract(x1.multiply(new BigInteger(2+""))).mod(p);
BigInteger y2=alpha.multiply(x1.subtract(x2)).subtract(y1).mod(p); for(int i=2;i<k.intValue();i++)
{
BigInteger ch=x2.subtract(x1);
if(ch.equals(new BigInteger(0+"")))
alpha=BigInteger.ZERO;
else
alpha=y2.subtract(y1).mod(p).multiply(ch.modInverse(p)).mod(p); BigInteger x3=alpha.multiply(alpha).subtract(x1).subtract(x2).mod(p); BigInteger y3=alpha.multiply(x1.subtract(x3)).subtract(y1).mod(p); x2=new BigInteger(x3.toString());
y2=new BigInteger(y3.toString());
}
return x2+" "+y2;
}
//Ellliptic curve cryptosystem simulation
public static void ecc(BigInteger a,BigInteger b,BigInteger p) throws Exception {
findPoints(p,a,b);
BufferedReader br=new BufferedReader(new FileReader("ans.txt")); StringTokenizer st=new StringTokenizer(br.readLine());
String m="100"+" "+"210";
br.readLine();
BigInteger secretA=new BigInteger(6+"");
BigInteger publicB=new BigInteger(8+"");
BigInteger x1=new BigInteger(st.nextToken());
BigInteger y1=new BigInteger(st.nextToken());
String nB=pointAdd(publicB,x1,y1,a,p);
String y0hint=pointAdd(secretA,x1,y1,a,p);
st=new StringTokenizer(nB);
String mask=pointAdd(secretA,new BigInteger(st.nextToken()),new BigInteger(st.nextToken()),a,p);
st=new StringTokenizer(mask);
StringTokenizer st1=new StringTokenizer(m);
BigInteger y11=new BigInteger(st.nextToken()).multiply(new BigInteger(st1.nextToken())).mod(p);
BigInteger y22=new BigInteger(st.nextToken()).multiply(new BigInteger(st1.nextToken())).mod(p);
st=new StringTokenizer(y0hint);
String nbY0=pointAdd(publicB,new BigInteger(st.nextToken()),new BigInteger(st.nextToken()),a,p);
st=new StringTokenizer(nbY0);
System.out.println("The chosen generator is "+x1+" "+y1+" The hint is "+y0hint+" The mask is "+mask+" y1 is "+y11+" y2 is "+y22+" nb*y0 is "+nbY0);
String m1=y11.multiply(new BigInteger(st.nextToken().trim()).modInverse(p)).mod(p).toString();
String m2=y22.multiply(new BigInteger(st.nextToken().trim()).modInverse(p)).mod(p).toString(); System.out.println(m1+" "+m2);
}
public static void main(String args[]) throws Exception {
BigInteger p=new BigInteger(23+"");
//findAB(p);
//findPoints(p,new BigInteger(2+""),new BigInteger(9+"")); //System.out.println(pointAdd(new BigInteger(10+""),new BigInteger(0+""),new BigInteger(3+""),new BigInteger(2+""),new BigInteger(23+"")));
ecc(new BigInteger(4+""),new BigInteger(9+""),p);
} }