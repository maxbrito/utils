package utils;

/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Vasiliy Vadimov
 * Creator: Person: Nuno Brito
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-12-22T13:34:56Z
 * LicenseName: Apache-2.0
 * FileName: TLSH.java  
 * FileCopyrightText: <text> Copyright 2014 Vasiliy Vadimov, Nuno Brito </text>
 * FileComment: <text> Java version of the TLSH similarity hashing algorithm </text> 
 */

public class TLSH {
	final private int	BUCKETS 		= 256;
	final private int	EFF_BUCKETS 		= 128;
	final private int	CODE_SIZE 		= 32;
	final private int	TLSH_CHECKSUM_LEN 	= 1;
	final private int	TLSH_STRING_LEN 	= 70;
	final private int	SLIDING_WND_SIZE 	= 5;
	final private int	RANGE_LVALUE		= 256;
	final private int	RANGE_QRATIO		= 16;
	final private float	LOG_1_5 = 0.4054651f;
	final private float	LOG_1_3 = 0.26236426f;
	final private float	LOG_1_1 = 0.095310180f;

	final private int []	vTable = {
		(int)1, (int)87, (int)49, (int)12, (int)176, (int)178, (int)102, (int)166, (int)121, (int)193, (int)6, (int)84, (int)249, (int)230, (int)44, (int)163,
		(int)14, (int)197, (int)213, (int)181, (int)161, (int)85, (int)218, (int)80, (int)64, (int)239, (int)24, (int)226, (int)236, (int)142, (int)38, (int)200,
		(int)110, (int)177, (int)104, (int)103, (int)141, (int)253, (int)255, (int)50, (int)77, (int)101, (int)81, (int)18, (int)45, (int)96, (int)31, (int)222,
		(int)25, (int)107, (int)190, (int)70, (int)86, (int)237, (int)240, (int)34, (int)72, (int)242, (int)20, (int)214, (int)244, (int)227, (int)149, (int)235,
		(int)97, (int)234, (int)57, (int)22, (int)60, (int)250, (int)82, (int)175, (int)208, (int)5, (int)127, (int)199, (int)111, (int)62, (int)135, (int)248,
		(int)174, (int)169, (int)211, (int)58, (int)66, (int)154, (int)106, (int)195, (int)245, (int)171, (int)17, (int)187, (int)182, (int)179, (int)0, (int)243,
		(int)132, (int)56, (int)148, (int)75, (int)128, (int)133, (int)158, (int)100, (int)130, (int)126, (int)91, (int)13, (int)153, (int)246, (int)216, (int)219,
		(int)119, (int)68, (int)223, (int)78, (int)83, (int)88, (int)201, (int)99, (int)122, (int)11, (int)92, (int)32, (int)136, (int)114, (int)52, (int)10,
		(int)138, (int)30, (int)48, (int)183, (int)156, (int)35, (int)61, (int)26, (int)143, (int)74, (int)251, (int)94, (int)129, (int)162, (int)63, (int)152,
		(int)170, (int)7, (int)115, (int)167, (int)241, (int)206, (int)3, (int)150, (int)55, (int)59, (int)151, (int)220, (int)90, (int)53, (int)23, (int)131,
		(int)125, (int)173, (int)15, (int)238, (int)79, (int)95, (int)89, (int)16, (int)105, (int)137, (int)225, (int)224, (int)217, (int)160, (int)37, (int)123,
		(int)118, (int)73, (int)2, (int)157, (int)46, (int)116, (int)9, (int)145, (int)134, (int)228, (int)207, (int)212, (int)202, (int)215, (int)69, (int)229,
		(int)27, (int)188, (int)67, (int)124, (int)168, (int)252, (int)42, (int)4, (int)29, (int)108, (int)21, (int)247, (int)19, (int)205, (int)39, (int)203,
		(int)233, (int)40, (int)186, (int)147, (int)198, (int)192, (int)155, (int)33, (int)164, (int)191, (int)98, (int)204, (int)165, (int)180, (int)117, (int)76,
		(int)140, (int)36, (int)210, (int)172, (int)41, (int)54, (int)159, (int)8, (int)185, (int)232, (int)113, (int)196, (int)231, (int)47, (int)146, (int)120,
		(int)51, (int)65, (int)28, (int)144, (int)254, (int)221, (int)93, (int)189, (int)194, (int)139, (int)112, (int)43, (int)71, (int)109, (int)184, (int)209
	};

	static private int [][] bitPairsDiffTable = null;

	private class LshBinStruct {
		public int [] 	checksum = new int [TLSH_CHECKSUM_LEN];
		public int	lValue = 0;
		public int	Q = 0;
		public int []	tmpCode = new int [CODE_SIZE];

		public int getQLo() {
			return (int)(Q & 0x0F);
		}

		public int getQHi() {
			return (int)((Q & 0xF0) >> 4);
		}

		public void setQLo(int x) {
			Q = (int)((Q & 0xF0) | (x & 0x0F));
		}

		public void setQHi(int x) {
			Q = (int)((Q & 0x0F) | ((x & 0x0F) << 4));
		}
	}

	private int []		aBucket = null;
	private int		dataLen = 0;
	private boolean		lshCodeValid = false; 
	LshBinStruct	lshBin = new LshBinStruct();
	private String		lshCode = null;

	/* non interface methods */

	private int swapByte(int a) {
		int ret = (int) (((a & 0xF0) >> 4) & 0x0F);
		ret |= ((a & 0x0F) << 4) & 0xF0;
		return ret;
	}

	private String toHex(int [] buf) {
		String ret = "";
		for (int i = 0; i < buf.length; i++) {
			if (buf[i] < 16)
				ret += "0";
			ret += Integer.toHexString(buf[i]);
		}
		return ret.toUpperCase();
	}

	private int bMapping(int salt, int i, int j, int k) {
		int h = 0;
		h = vTable[h ^ salt];
		h = vTable[h ^ i];
		h = vTable[h ^ j];
		h = vTable[h ^ k];
		return h;
	}

	private int partition(int [] buf, int left, int right) {
		if (left == right) {
			return left;
		}
		if (left + 1 == right) {
			if (buf[left] > buf[right]) {
				int tmp = buf[left];
				buf[left] = buf[right];
				buf[right] = tmp;
			}
			return left;
		}
			
		int ret = left, pivot = (left + right) >> 1;
		
		int val = buf[pivot];
		
		buf[pivot] = buf[right];
		buf[right] = val;
		
		for (int i = left; i < right; i++) {
			if (buf[i] < val) {
				int tmp = buf[ret];
				buf[ret] = buf[i];
				buf[i] = tmp;
				ret++;
			}
		}
		buf[right] = buf[ret];
		buf[ret] = val;
		
		return ret;
	}

	private int [] findQuartile(int [] aBucket) {
		int [] bucketCopy    = new int [EFF_BUCKETS];
		int [] shortCutLeft  = new int [EFF_BUCKETS];
		int [] shortCutRight = new int [EFF_BUCKETS];
		int spl = 0, spr = 0;
		int q1 = 0, q2 = 0, q3 = 0;

		int p1 = EFF_BUCKETS / 4 - 1;
		int p2 = EFF_BUCKETS / 2 - 1;
		int p3 = EFF_BUCKETS - EFF_BUCKETS / 4 - 1;
		int end = EFF_BUCKETS - 1;
	
                System.arraycopy(aBucket, 0, bucketCopy, 0, EFF_BUCKETS);
	
		for (int l = 0, r = end; ;) {
			int ret = partition(bucketCopy, l, r);
			if (ret > p2) {
				r = ret - 1;
				shortCutRight[spr] = ret;
				spr++;
			} else if (ret < p2) {
				l = ret + 1;
				shortCutLeft[spl] = ret;
				spl++;
			} else {
				q2 = bucketCopy[p2];
				break;
			}
		}
		
		shortCutLeft[spl]  = p2 - 1;
		shortCutRight[spr] = p2 + 1;
	
		for (int i = 0, l = 0; i <= spl; i++) {
			int r = shortCutLeft[i];
			if (r > p1) {
				while (true) {
					int ret = partition(bucketCopy, l, r);
					if (ret > p1) {
						r = ret - 1;
					} else if (ret < p1) {
						l = ret + 1;
					} else {
						q1 = bucketCopy[p1];
						break;
					}
				}
				break;
			} else if (r < p1) {
				l = r;
			} else {
				q1 = bucketCopy[p1];
				break;
			}
		}
	
		for (int i = 0, r = end; i <= spr; i++) {
			int l = shortCutRight[i];
			if (l < p3) {
				while (true) {
					int ret = partition(bucketCopy, l, r);
					if (ret > p3) {
						r = ret - 1;
					} else if (ret < p3) {
						l = ret + 1;
					} else {
						q3 = bucketCopy[p3];
						break;
					}
				}
				break;
			} else if (l > p3) {
				r = l;
			} else {
				q3 = bucketCopy[p3];
				break;
			}
		}
		return new int [] {q1, q2, q3};
	}

	private int lCapturing(int len) {
		int i;
		if (len <= 656) {
			i = (int)Math.floor(Math.log((float) len) / LOG_1_5);
		} else if( len <= 3199 ) {
			i = (int)Math.floor(Math.log((float) len) / LOG_1_3 - 8.72777);
		} else {
			i = (int)Math.floor(Math.log((float) len) / LOG_1_1 - 62.5472 );
		}
		
		return (int) (i & 0xFF);
	}
	
	private int modDiff(int x, int y, int R){
		int dl;
		int dr;
		if (y > x){
			dl = (y - x);
			dr = (x + R - y);
		} else {
			dl = (x - y);
			dr = (y + R - x);
		}
		if (dl > dr)
			return dr;
		return dl;
	}

	private int hDistance(int [] x, int [] y)
	{
		int diff = 0;
		for (int i = 0; i < x.length; i++) {
			diff += bitPairsDiffTable[x[i]][y[i]];
		}
		return diff;
	}

	/* class interface, public methods */

	public TLSH() {
		/* read data from file */
		if (bitPairsDiffTable == null) {
			bitPairsDiffTable = new int [256][256];
			for (int i = 0; i < 256; i++) {
				for (int j = 0; j < 256; j++) {
					int x = i, y = j, d, diff = 0;
					d = Math.abs(x % 4 - y % 4); diff += (d == 3 ? 6 : d);
					x /= 4; y /= 4;
					d = Math.abs(x % 4 - y % 4); diff += (d == 3 ? 6 : d);
					x /= 4; y /= 4;
					d = Math.abs(x % 4 - y % 4); diff += (d == 3 ? 6 : d);
					x /= 4; y /= 4;
					d = Math.abs(x % 4 - y % 4); diff += (d == 3 ? 6 : d);
					bitPairsDiffTable[i][j] = diff;
				}
			}
		}
	}

	public void update(String data) {
		int j = this.dataLen % SLIDING_WND_SIZE;
		int fedLen = this.dataLen;
		if (this.aBucket == null) {
			this.aBucket = new int [BUCKETS];
		}
		int [] slideWindow = new int [SLIDING_WND_SIZE];
		for (int i = 0; i < data.length(); i++) {
			slideWindow[j] = (int)data.charAt(i);
			if (fedLen >= 4) {
				int j1 = (j + SLIDING_WND_SIZE - 1) % SLIDING_WND_SIZE;
				int j2 = (j + SLIDING_WND_SIZE - 2) % SLIDING_WND_SIZE;
				int j3 = (j + SLIDING_WND_SIZE - 3) % SLIDING_WND_SIZE;
				int j4 = (j + SLIDING_WND_SIZE - 4) % SLIDING_WND_SIZE;
				for (int k = 0; k < TLSH_CHECKSUM_LEN; k++) {
					if (k == 0) {
						this.lshBin.checksum[k] = bMapping((int) 0, slideWindow[j], slideWindow[j1], this.lshBin.checksum[k]);
					} else {
						this.lshBin.checksum[k] = bMapping(this.lshBin.checksum[k - 1], slideWindow[j], slideWindow[j1], this.lshBin.checksum[k]);
					}
				}
				int r;
			   	r = bMapping((int)2, slideWindow[j], slideWindow[j1], slideWindow[j2]);
				this.aBucket[r]++;
				r = bMapping((int)3, slideWindow[j], slideWindow[j1], slideWindow[j3]);
				this.aBucket[r]++;
				r = bMapping((int)5, slideWindow[j], slideWindow[j2], slideWindow[j3]);
				this.aBucket[r]++;
				r = bMapping((int) 7, slideWindow[j], slideWindow[j2], slideWindow[j4]);
				this.aBucket[r]++;
				r = bMapping((int) 11, slideWindow[j], slideWindow[j1], slideWindow[j4]);
				this.aBucket[r]++;
				r = bMapping((int) 13, slideWindow[j], slideWindow[j3], slideWindow[j4]);
				this.aBucket[r]++;
			}

			fedLen++;
			j = (j + SLIDING_WND_SIZE + 1) % SLIDING_WND_SIZE;
		}
		this.dataLen += data.length();
	}

	public void update(byte [] data) {
		int j = this.dataLen % SLIDING_WND_SIZE;
		int fedLen = this.dataLen;
		if (this.aBucket == null) {
			this.aBucket = new int [BUCKETS];
		}
		int [] slideWindow = new int [SLIDING_WND_SIZE];
		for (int i : data) {
			slideWindow[j] = i;
			if (fedLen >= 4) {
				int j1 = (j + SLIDING_WND_SIZE - 1) % SLIDING_WND_SIZE;
				int j2 = (j + SLIDING_WND_SIZE - 2) % SLIDING_WND_SIZE;
				int j3 = (j + SLIDING_WND_SIZE - 3) % SLIDING_WND_SIZE;
				int j4 = (j + SLIDING_WND_SIZE - 4) % SLIDING_WND_SIZE;
				for (int k = 0; k < TLSH_CHECKSUM_LEN; k++) {
					if (k == 0) {
						this.lshBin.checksum[k] = bMapping(0, slideWindow[j], slideWindow[j1], this.lshBin.checksum[k]);
					} else {
						this.lshBin.checksum[k] = bMapping(this.lshBin.checksum[k - 1], slideWindow[j], slideWindow[j1], this.lshBin.checksum[k]);
					}
				}
				int r;
			   	r = bMapping((int)2, slideWindow[j], slideWindow[j1], slideWindow[j2]);
				this.aBucket[r]++;
				r = bMapping((int)3, slideWindow[j], slideWindow[j1], slideWindow[j3]);
				this.aBucket[r]++;
				r = bMapping((int)5, slideWindow[j], slideWindow[j2], slideWindow[j3]);
				this.aBucket[r]++;
				r = bMapping((int) 7, slideWindow[j], slideWindow[j2], slideWindow[j4]);
				this.aBucket[r]++;
				r = bMapping((int) 11, slideWindow[j], slideWindow[j1], slideWindow[j4]);
				this.aBucket[r]++;
				r = bMapping((int) 13, slideWindow[j], slideWindow[j3], slideWindow[j4]);
				this.aBucket[r]++;
			}

			fedLen++;
			j = (j + SLIDING_WND_SIZE + 1) % SLIDING_WND_SIZE;
		}
		this.dataLen += data.length;
	}
	public void finale() { /* word final is reserved */
		if (this.dataLen < 512) {
			this.aBucket = null;
			return;
		}

		int q1, q2, q3;
		int [] tmp = findQuartile(this.aBucket);
		q1 = tmp[0];
		q2 = tmp[1];
		q3 = tmp[2];

		int nonzero = 0;
		for (int i = 0; i < CODE_SIZE; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.aBucket[4*i + j] > 0) {
					nonzero++;
				}
			}
		}
		if (nonzero <= 2 * CODE_SIZE) {
			aBucket = null;
			return;
		}

		for (int i = 0; i < CODE_SIZE; i++) {
			int h = 0;
			for (int j = 0; j < 4; j++) {
				int k = this.aBucket[4 * i + j];
				if (q3 < k) {
					h += 3 << (j * 2);
				} else if (q2 < k) {
					h += 2 << (j * 2);
				} else if (q1 < k) {
					h += 1 << (j * 2);
				}
			}
			this.lshBin.tmpCode[i] = h;
		}

		this.aBucket = null;

		this.lshBin.lValue = lCapturing(this.dataLen);
		this.lshBin.setQLo((int) (((float)(q1 * 100.f) / (float) q3) % 16));
		this.lshBin.setQHi((int) (((float)(q2 * 100.f) / (float) q3) % 16));
		this.lshCodeValid = true;   
	}

	public String hash() {
		if (this.lshCode != null) {
			return this.lshCode;
		}
		if (!this.lshCodeValid) {
			return null;
		}
		LshBinStruct tmp = new LshBinStruct();
		for (int k = 0; k < TLSH_CHECKSUM_LEN; k++) {
			tmp.checksum[k] = swapByte(this.lshBin.checksum[k]);
		}
		tmp.lValue = swapByte(this.lshBin.lValue);
		tmp.Q = swapByte(this.lshBin.Q);
		for (int i = 0; i < CODE_SIZE; i++) {
			tmp.tmpCode[i] = this.lshBin.tmpCode[CODE_SIZE - 1 - i];
		}
		this.lshCode = toHex(tmp.checksum);
		this.lshCode += toHex(new int [] {tmp.lValue});
		this.lshCode += toHex(new int [] {tmp.Q});
		this.lshCode += toHex(tmp.tmpCode);
		return this.lshCode;
	}

	public boolean compare(TLSH other) {
		return this.lshCode.equals(other.lshCode);
	}

	final public int totalDiff(TLSH other, boolean lenDiff) {
		int diff = 0;
		
		if (lenDiff) {
			int ldiff = modDiff(lshBin.lValue, other.lshBin.lValue, RANGE_LVALUE);
			if (ldiff == 0)
				diff = 0;
			else if (ldiff == 1)
				diff = 1;
			else
				diff += ldiff * 12;
		}
		
		int q1diff = modDiff(lshBin.getQLo(), other.lshBin.getQLo(), RANGE_QRATIO);
		if (q1diff <= 1)
			diff += q1diff;
		else		   
			diff += (q1diff - 1) * 12;
		
		int q2diff = modDiff(lshBin.getQHi(), other.lshBin.getQHi(), RANGE_QRATIO);
		if (q2diff <= 1)
			diff += q2diff;
		else
			diff += (q2diff - 1) * 12;
		
		for (int k = 0; k < TLSH_CHECKSUM_LEN; k++) {	
			if (lshBin.checksum[k] != other.lshBin.checksum[k]) {
				diff++;
				break;
			}
		}
		
		diff += hDistance(lshBin.tmpCode, other.lshBin.tmpCode);
	
		return diff;
	}

	/* this method for testing only */
	/* coincides with simple_unit */
	public static void main(String [] args) {
		try {
			TLSH ti1 = new TLSH();
			TLSH ti2 = new TLSH();

			String s = "This is a test for Jon Oliver. This is a string. Hello Hello Hello ";
			while (s.length() < 1023) {
				s += (char)('A' + s.length() % 26);
			}
			s += (char)0;
			ti2.update(s);
			ti2.finale();

			s = "This is a test for Lili Diao. This is a string. Hello Hello Hello ";
			while (s.length() < 511) {
				s += (char)('A' + s.length() % 26);
			}
			s += (char)0;
			ti1.update(s);
			ti1.finale();

			System.out.println("hash1 = " + ti1.hash());
			System.out.println("hash2 = " + ti2.hash());

			System.out.println("difference (same strings) = " + ti1.totalDiff(ti1, true));
			System.out.println("difference (with len) = " + ti1.totalDiff(ti2, true));
			System.out.println("difference (without len) = " + ti1.totalDiff(ti2, false));

			s = "fgsfds";
			for (int i = 0; i < 100000; i++)
				s += (char)('A' + i % 26);
			TLSH ti3 = new TLSH();
			ti3.update(s);
			ti3.finale();
			System.out.println("hash3 = " + ti3.hash());
			
			
			System.out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
