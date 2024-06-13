package org.example;

import java.io.File;
import java.util.Arrays;

public class Matrix implements Cloneable
{

    private int[][] werte;


    public Matrix(int n) throws MatrixException
    {
        if (n < 0)
        {
            throw new MatrixException("Fehlermeldung: Zeilen und Spalten müssen positiv sein");
        }
        this.werte = new int[n][n];
    }

    public int getWert(int zeile, int spalte)
    {
        return this.werte[zeile][spalte];
    }

    public void setWert(int wert, int zeile, int spalte)
    {
        if (wert < 0)
        {
            return;
        }
        this.werte[zeile][spalte] = wert;
    }

    public Matrix mult(Matrix other) throws MatrixException
    {
        Matrix multipliedMatrix = new Matrix(this.werte.length);

        for (int i = 0; i < this.werte.length; i++)
        {
            for (int j = 0; j < this.werte.length; j++)
            {
                multipliedMatrix.werte[i][j] = 0;

                for (int k = 0; k < this.werte.length; k++)
                {
                    multipliedMatrix.werte[i][j] += this.werte[i][k] * other.werte[k][j];
                }
            }
        }
        return multipliedMatrix;
    }

    public Matrix copy()
    {
        return this.clone();
    }

    public Matrix add(Matrix other) throws MatrixException
    {
        Matrix addedMatrix = new Matrix(werte.length);
        for (int i = 0; i < werte.length; i++)
        {
            for (int j = 0; j < werte[i].length; j++)
            {
                addedMatrix.setWert((werte[i][j] + other.getWert(i, j)), i, j);
            }
        }
        return addedMatrix;
    }

    public int maxVonZeile(int zeile)
    {
        int max = 0;
        for (int i = 0; i < this.werte.length; i++)
        {
            if (werte[zeile-1][i] > max)
            {
                max = werte[zeile-1][i];
            }
        }
        return max;
    }

    public int minVonZeileNot0(int zeile)
    {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < this.werte.length; i++)
        {
            if (werte[zeile-1][i] < min && werte[zeile-1][i] != 0)
            {
                min = werte[zeile-1][i];
            }
        }
        return min;
    }

    public int[] knotenGrad()
    {
        int[] knotenGrade = new int[this.werte.length];

        for (int i = 0; i < this.werte.length; i++)
        {
            for (int j = 0; j < this.werte.length; j++)
            {
                knotenGrade[i] += this.werte[i][j];
            }
        }
        return knotenGrade;
    }

    public int anzahlKantenGesamt()
    {
        int anzahl = 0;
        for (int i = 0; i < this.werte.length; i++)
        {
            for (int j = 0; j < this.werte.length; j++)
            {
                if (this.werte[i][j] == 1)
                {
                    anzahl++;
                }
            }
        }

        return anzahl / 2;
    }

    public int arrayLength()
    {
        return werte.length;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix matrix = (Matrix) o;

        return Arrays.deepEquals(werte, matrix.werte);
    }

    @Override
    public int hashCode()
    {
        return Arrays.deepHashCode(werte);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.werte.length; i++)
        {
            for (int j = 0; j < this.werte[i].length; j++)
            {
                sb.append(this.werte[i][j]).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
        }

        return sb.toString();
    }

    public String toStringCsv()
    {
        String delimiter = File.pathSeparator;
        StringBuilder sb = new StringBuilder();
        for (int[] ints : this.werte)
        {
            for (int anInt : ints)
            {
                sb.append(anInt).append(delimiter);
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public void print()
    {
        System.out.println(this);
    }


    @Override
    public Matrix clone()
    {
        try
        {
            Matrix clone = (Matrix) super.clone();
            clone.werte = this.werte.clone();
            for (int i = 0; i < clone.werte.length; i++)
            {
                clone.werte[i] = this.werte[i].clone();
            }
            return clone;
        }
        catch (CloneNotSupportedException e)
        {
            throw new AssertionError();
        }
    }

    /**
     * Diese Methode soll nur in der Testklasse verwendet werden!
     * @param werte
     */
    public Matrix(int[][] werte)
    {
        this.werte = werte;
    }
}
