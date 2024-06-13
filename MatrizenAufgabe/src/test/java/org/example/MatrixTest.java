package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest
{
    private Matrix a;
    private Matrix b;

    @org.junit.jupiter.api.BeforeEach
    void setUp()
    {
        int[][] tmpA = {       {1, 2, 3},
                {3, 4, 5},
                {1, 0, 4} };
        a = new Matrix(tmpA);
        int[][] tmpB = {       {5, 1, 1},
                {3, 1, 3},
                {1, 0, 4} };

        b = new Matrix(tmpB);
    }

    @Test
    void constructor()
    {
        try
        {
            Matrix c = new Matrix(5);
            assertEquals(5, c.arrayLength());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an exception");
        }
    }

    @org.junit.jupiter.api.Test
    void getWert()
    {
        assertEquals(1, a.getWert(0,0));
        assertEquals(4, a.getWert(1,1));
        assertEquals(5, a.getWert(1,2));
    }

    @org.junit.jupiter.api.Test
    void setWert()
    {
        a.setWert(8, 1, 2);
        assertEquals(8, a.getWert(1,2));
    }

    @org.junit.jupiter.api.Test
    void mult()
    {
        try
        {
            Matrix c = a.mult(b);
            int[][] tmpC = {{14, 3, 19},
                    {32, 7, 35},
                    {9, 1, 17}}; // TODO nochmals pruefen!

            Matrix expResultMul = new Matrix(tmpC);
            assert (c.equals(expResultMul));

            c.print();
        }
        catch (Exception e)
        {
            fail("There shouldn't be an exception");
        }
    }

    @org.junit.jupiter.api.Test
    void copy()
    {
        try
        {
            Matrix c = a.copy();

            assert (c.equals(a));

            for (int i = 0; i < a.arrayLength(); i++)
            {
                for (int j = 0; j < a.arrayLength(); j++)
                {
                    //System.out.print(c.getWert(i, j));
                    assertEquals(a.getWert(i, j), c.getWert(i, j));
                }
                //System.out.println();
            }
        }
        catch (Exception e)
        {
            fail("There shouldn't be an exception");
        }
    }

    @org.junit.jupiter.api.Test
    void add()
    {
        try
        {
            Matrix c = a.add(b);
            int[][] tmpC = {
                    {6, 3, 4},
                    {6, 5, 8},
                    {2, 0, 8}
            };

            Matrix expResult = new Matrix(tmpC);

            assert (c.equals(expResult));
        }
        catch (Exception e)
        {
            fail("There shouldn't be an exception");
        }
    }

    @org.junit.jupiter.api.Test
    void maxVonZeile()
    {
        assertEquals(5, a.maxVonZeile(2));
    }

    @Test
    void minVonZeile()
    {
        assertEquals(1, a.minVonZeileNot0(1));
    }

    @Test
    void knotenGrad()
    {
        int[][] knotenTest =
        {
            {0, 1, 1, 0},
            {1, 0, 1, 0},
            {1, 1, 0, 1},
            {0, 0, 1, 0}
        };
        Matrix knoten = new Matrix(knotenTest);

        int[] knotenArray = {2, 2, 3, 1};

        assertTrue(Arrays.equals(knotenArray, knoten.knotenGrad()));
    }

    @Test
    void StringOfToString()
    {
        String str = "1,2,3\n3,4,5\n1,0,4\n";
        assertEquals(str, a.toString());
    }
}