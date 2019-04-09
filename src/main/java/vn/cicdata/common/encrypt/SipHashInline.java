package vn.cicdata.common.encrypt;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;

public class SipHashInline {

    public static long hash48(long k0, long k1, byte[] data) {
        long v0 = 0x736f6d6570736575L ^ k0;
        long v1 = 0x646f72616e646f6dL ^ k1;
        long v2 = 0x6c7967656e657261L ^ k0;
        long v3 = 0x7465646279746573L ^ k1;
        long m;
        int last = data.length / 8 * 8;
        int i = 0;

        // processing 8 bytes blocks in data
        while (i < last) {
            // pack a block to long, as LE 8 bytes
            m = data[i++] & 0xffL |
                    (data[i++] & 0xffL) << 8 |
                    (data[i++] & 0xffL) << 16 |
                    (data[i++] & 0xffL) << 24 |
                    (data[i++] & 0xffL) << 32 |
                    (data[i++] & 0xffL) << 40 |
                    (data[i++] & 0xffL) << 48 |
                    (data[i++] & 0xffL) << 56;
            // MSGROUND {
            v3 ^= m;

            /* SIPROUND wih hand reordering
             *
             * SIPROUND in siphash24.c:
             *   A: v0 += v1;
             *   B: v1=ROTL(v1,13);
             *   C: v1 ^= v0;
             *   D: v0=ROTL(v0,32);
             *   E: v2 += v3;
             *   F: v3=ROTL(v3,16);
             *   G: v3 ^= v2;
             *   H: v0 += v3;
             *   I: v3=ROTL(v3,21);
             *   J: v3 ^= v0;
             *   K: v2 += v1;
             *   L: v1=ROTL(v1,17);
             *   M: v1 ^= v2;
             *   N: v2=ROTL(v2,32);
             *
             * Each dependency:
             *   B -> A
             *   C -> A, B
             *   D -> C
             *   F -> E
             *   G -> E, F
             *   H -> D, G
             *   I -> H
             *   J -> H, I
             *   K -> C, G
             *   L -> K
             *   M -> K, L
             *   N -> M
             *
             * Dependency graph:
             *   D -> C -> B -> A
             *        G -> F -> E
             *   J -> I -> H -> D, G
             *   N -> M -> L -> K -> C, G
             *
             * Resulting parallel friendly execution order:
             *   -> ABCDHIJ
             *   -> EFGKLMN
             */

            // SIPROUND {
            v0 += v1;
            v2 += v3;
            v1 = (v1 << 13) | v1 >>> 51;
            v3 = (v3 << 16) | v3 >>> 48;
            v1 ^= v0;
            v3 ^= v2;
            v0 = (v0 << 32) | v0 >>> 32;
            v2 += v1;
            v0 += v3;
            v1 = (v1 << 17) | v1 >>> 47;
            v3 = (v3 << 21) | v3 >>> 43;
            v1 ^= v2;
            v3 ^= v0;
            v2 = (v2 << 32) | v2 >>> 32;
            // }
            // SIPROUND {
            v0 += v1;
            v2 += v3;
            v1 = (v1 << 13) | v1 >>> 51;
            v3 = (v3 << 16) | v3 >>> 48;
            v1 ^= v0;
            v3 ^= v2;
            v0 = (v0 << 32) | v0 >>> 32;
            v2 += v1;
            v0 += v3;
            v1 = (v1 << 17) | v1 >>> 47;
            v3 = (v3 << 21) | v3 >>> 43;
            v1 ^= v2;
            v3 ^= v0;
            v2 = (v2 << 32) | v2 >>> 32;
            // }
            // SIPROUND {
            v0 += v1;
            v2 += v3;
            v1 = (v1 << 13) | v1 >>> 51;
            v3 = (v3 << 16) | v3 >>> 48;
            v1 ^= v0;
            v3 ^= v2;
            v0 = (v0 << 32) | v0 >>> 32;
            v2 += v1;
            v0 += v3;
            v1 = (v1 << 17) | v1 >>> 47;
            v3 = (v3 << 21) | v3 >>> 43;
            v1 ^= v2;
            v3 ^= v0;
            v2 = (v2 << 32) | v2 >>> 32;
            // }
            // SIPROUND {
            v0 += v1;
            v2 += v3;
            v1 = (v1 << 13) | v1 >>> 51;
            v3 = (v3 << 16) | v3 >>> 48;
            v1 ^= v0;
            v3 ^= v2;
            v0 = (v0 << 32) | v0 >>> 32;
            v2 += v1;
            v0 += v3;
            v1 = (v1 << 17) | v1 >>> 47;
            v3 = (v3 << 21) | v3 >>> 43;
            v1 ^= v2;
            v3 ^= v0;
            v2 = (v2 << 32) | v2 >>> 32;
            // }
            v0 ^= m;
            // }
        }

        // packing the last block to long, as LE 0-7 bytes + the length in the top byte
        m = 0;
        for (i = data.length - 1; i >= last; --i) {
            m <<= 8;
            m |= (data[i] & 0xffL);
        }
        m |= (long) data.length << 56;
        // MSGROUND {
        v3 ^= m;
        // SIPROUND {
        v0 += v1;
        v2 += v3;
        v1 = (v1 << 13) | v1 >>> 51;
        v3 = (v3 << 16) | v3 >>> 48;
        v1 ^= v0;
        v3 ^= v2;
        v0 = (v0 << 32) | v0 >>> 32;
        v2 += v1;
        v0 += v3;
        v1 = (v1 << 17) | v1 >>> 47;
        v3 = (v3 << 21) | v3 >>> 43;
        v1 ^= v2;
        v3 ^= v0;
        v2 = (v2 << 32) | v2 >>> 32;
        // }
        // SIPROUND {
        v0 += v1;
        v2 += v3;
        v1 = (v1 << 13) | v1 >>> 51;
        v3 = (v3 << 16) | v3 >>> 48;
        v1 ^= v0;
        v3 ^= v2;
        v0 = (v0 << 32) | v0 >>> 32;
        v2 += v1;
        v0 += v3;
        v1 = (v1 << 17) | v1 >>> 47;
        v3 = (v3 << 21) | v3 >>> 43;
        v1 ^= v2;
        v3 ^= v0;
        v2 = (v2 << 32) | v2 >>> 32;
        // }
        // SIPROUND {
        v0 += v1;
        v2 += v3;
        v1 = (v1 << 13) | v1 >>> 51;
        v3 = (v3 << 16) | v3 >>> 48;
        v1 ^= v0;
        v3 ^= v2;
        v0 = (v0 << 32) | v0 >>> 32;
        v2 += v1;
        v0 += v3;
        v1 = (v1 << 17) | v1 >>> 47;
        v3 = (v3 << 21) | v3 >>> 43;
        v1 ^= v2;
        v3 ^= v0;
        v2 = (v2 << 32) | v2 >>> 32;
        // }
        // SIPROUND {
        v0 += v1;
        v2 += v3;
        v1 = (v1 << 13) | v1 >>> 51;
        v3 = (v3 << 16) | v3 >>> 48;
        v1 ^= v0;
        v3 ^= v2;
        v0 = (v0 << 32) | v0 >>> 32;
        v2 += v1;
        v0 += v3;
        v1 = (v1 << 17) | v1 >>> 47;
        v3 = (v3 << 21) | v3 >>> 43;
        v1 ^= v2;
        v3 ^= v0;
        v2 = (v2 << 32) | v2 >>> 32;
        // }
        v0 ^= m;
        // }

        // finishing...
        v2 ^= 0xff;
        // SIPROUND {
        v0 += v1;
        v2 += v3;
        v1 = (v1 << 13) | v1 >>> 51;
        v3 = (v3 << 16) | v3 >>> 48;
        v1 ^= v0;
        v3 ^= v2;
        v0 = (v0 << 32) | v0 >>> 32;
        v2 += v1;
        v0 += v3;
        v1 = (v1 << 17) | v1 >>> 47;
        v3 = (v3 << 21) | v3 >>> 43;
        v1 ^= v2;
        v3 ^= v0;
        v2 = (v2 << 32) | v2 >>> 32;
        // }
        // SIPROUND {
        v0 += v1;
        v2 += v3;
        v1 = (v1 << 13) | v1 >>> 51;
        v3 = (v3 << 16) | v3 >>> 48;
        v1 ^= v0;
        v3 ^= v2;
        v0 = (v0 << 32) | v0 >>> 32;
        v2 += v1;
        v0 += v3;
        v1 = (v1 << 17) | v1 >>> 47;
        v3 = (v3 << 21) | v3 >>> 43;
        v1 ^= v2;
        v3 ^= v0;
        v2 = (v2 << 32) | v2 >>> 32;
        // }
        // SIPROUND {
        v0 += v1;
        v2 += v3;
        v1 = (v1 << 13) | v1 >>> 51;
        v3 = (v3 << 16) | v3 >>> 48;
        v1 ^= v0;
        v3 ^= v2;
        v0 = (v0 << 32) | v0 >>> 32;
        v2 += v1;
        v0 += v3;
        v1 = (v1 << 17) | v1 >>> 47;
        v3 = (v3 << 21) | v3 >>> 43;
        v1 ^= v2;
        v3 ^= v0;
        v2 = (v2 << 32) | v2 >>> 32;
        // }
        // SIPROUND {
        v0 += v1;
        v2 += v3;
        v1 = (v1 << 13) | v1 >>> 51;
        v3 = (v3 << 16) | v3 >>> 48;
        v1 ^= v0;
        v3 ^= v2;
        v0 = (v0 << 32) | v0 >>> 32;
        v2 += v1;
        v0 += v3;
        v1 = (v1 << 17) | v1 >>> 47;
        v3 = (v3 << 21) | v3 >>> 43;
        v1 ^= v2;
        v3 ^= v0;
        v2 = (v2 << 32) | v2 >>> 32;
        // }
        // SIPROUND {
        v0 += v1;
        v2 += v3;
        v1 = (v1 << 13) | v1 >>> 51;
        v3 = (v3 << 16) | v3 >>> 48;
        v1 ^= v0;
        v3 ^= v2;
        v0 = (v0 << 32) | v0 >>> 32;
        v2 += v1;
        v0 += v3;
        v1 = (v1 << 17) | v1 >>> 47;
        v3 = (v3 << 21) | v3 >>> 43;
        v1 ^= v2;
        v3 ^= v0;
        v2 = (v2 << 32) | v2 >>> 32;
        // }
        // SIPROUND {
        v0 += v1;
        v2 += v3;
        v1 = (v1 << 13) | v1 >>> 51;
        v3 = (v3 << 16) | v3 >>> 48;
        v1 ^= v0;
        v3 ^= v2;
        v0 = (v0 << 32) | v0 >>> 32;
        v2 += v1;
        v0 += v3;
        v1 = (v1 << 17) | v1 >>> 47;
        v3 = (v3 << 21) | v3 >>> 43;
        v1 ^= v2;
        v3 ^= v0;
        v2 = (v2 << 32) | v2 >>> 32;
        // }
        // SIPROUND {
        v0 += v1;
        v2 += v3;
        v1 = (v1 << 13) | v1 >>> 51;
        v3 = (v3 << 16) | v3 >>> 48;
        v1 ^= v0;
        v3 ^= v2;
        v0 = (v0 << 32) | v0 >>> 32;
        v2 += v1;
        v0 += v3;
        v1 = (v1 << 17) | v1 >>> 47;
        v3 = (v3 << 21) | v3 >>> 43;
        v1 ^= v2;
        v3 ^= v0;
        v2 = (v2 << 32) | v2 >>> 32;
        // }
        // SIPROUND {
        v0 += v1;
        v2 += v3;
        v1 = (v1 << 13) | v1 >>> 51;
        v3 = (v3 << 16) | v3 >>> 48;
        v1 ^= v0;
        v3 ^= v2;
        v0 = (v0 << 32) | v0 >>> 32;
        v2 += v1;
        v0 += v3;
        v1 = (v1 << 17) | v1 >>> 47;
        v3 = (v3 << 21) | v3 >>> 43;
        v1 ^= v2;
        v3 ^= v0;
        v2 = (v2 << 32) | v2 >>> 32;
        // }
        return v0 ^ v1 ^ v2 ^ v3;
    }


    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(x);


//        ByteBuffer  bb = ByteBuffer.allocate(7);
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//        bb.putLong()


        return buffer.array();
    }

    public static String hash(long k0, long k1, long msisdn) {
//        long k0 = 448862796;
//        long k1 = 13335914;
        byte[] p = longToBytes(msisdn);
        long hashValue = hash48(k0, k1, p);
        byte[] encodedBytes = Base64.getEncoder().encode(longToBytes(hashValue));
        return new String(encodedBytes);
    }

//    public static byte[]  getLong() {
//        long k0 = 448862796;
//        long k1 = 13335914;
//        long msisdn = 84167363850L;
//        String hashValue = hash(k0, k1, msisdn);
//        byte[] x = Base64.getDecoder().decode(hashValue);
//
//        return x;
//    }
}
