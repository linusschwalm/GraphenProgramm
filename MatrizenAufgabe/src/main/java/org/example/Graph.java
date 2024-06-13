package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Graph
{

    protected Matrix a; // Adjazenz
    protected Matrix p; // aktuelle Potenz
    protected Matrix d; // Distanz
    protected Matrix w; // Weg

    public Graph(Matrix adjazenz)
    {
        this.a = adjazenz;
    }

    public Graph(String filename) throws MatrixException
    {
        this.a = this.importMatrixCsv(filename);
    }

    public void berechneMatrizen() throws MatrixException
    {
        berechnePotenzMatrix(2);
        berechneDistanzMatrix();
        berechneWegMatrix();
    }

    public void berechneAdjazenzmatrix(String filename) throws MatrixException
    {
        this.a = this.importMatrixCsv(filename);
    }

    public void berechnePotenzMatrix(int potenz) throws MatrixException
    {
        Matrix temp = this.a.clone();

        for (int i = 1; i < potenz; i++)
        {
            temp = a.mult(temp);
        }

        this.p = temp;
    }

    public void berechneDistanzMatrix() throws MatrixException
    {
        this.d = this.a.clone();
        for (int i = 1; i <= this.d.arrayLength(); i++)
        {
            berechnePotenzMatrix(i);

            for (int j = 0; j < this.d.arrayLength(); j++)
            {
                for (int k = 0; k < this.d.arrayLength(); k++)
                {
                    if (this.d.getWert(j, k) == 0 && this.p.getWert(j, k) != 0)
                    {
                        this.d.setWert(i, j, k);
                    }
                }
            }
        }
        for (int i = 0; i < this.d.arrayLength(); i++)
        {
            this.d.setWert(0, i, i);
        }
    }

    public void berechneWegMatrix() throws MatrixException
    {
        this.w = this.a.clone();
        for (int i = 0; i < this.w.arrayLength(); i++)
        {
            this.w.setWert(1, i, i);
        }
        for (int i = 1; i <= this.w.arrayLength(); i++)
        {
            berechnePotenzMatrix(i);

            for (int j = 0; j < this.w.arrayLength(); j++)
            {
                for (int k = 0; k < this.w.arrayLength(); k++)
                {
                    if (this.w.getWert(j, k) == 0 && this.p.getWert(j, k) != 0)
                    {
                        this.w.setWert(1, j, k);
                    }
                }
            }
        }
    }

    // return als int[], print zum Anschauen
    public int[] berechneExzentrizitaeten() throws MatrixException
    {
        StringBuilder sb = new StringBuilder();
        berechneDistanzMatrix();
        int[] exzentrizitaeten = new int[this.a.arrayLength()];

        for (int i = 0; i < this.a.arrayLength(); i++)
        {
            int exz = 0;

            for (int j = 0; j < this.d.arrayLength(); j++)
            {
                if (this.d.getWert(i, j) > exz)
                {
                    exz = this.d.getWert(i, j);
                }
            }

            exzentrizitaeten[i] = exz;
            sb.append("Exzentrizitaet von ").append(i + 1).append(": ").append(exz).append("\n");
        }
        System.out.println(sb);
        return exzentrizitaeten;
    }

    public int berechneRadius() throws MatrixException
    {
        berechneDistanzMatrix();
        int radius = Integer.MAX_VALUE;
        int[] exz = this.berechneExzentrizitaeten();

        for (int j : exz)
        {
            if (j < radius)
            {
                radius = j;
            }
        }

        return radius;
    }

    public int berechneDurchmesser() throws MatrixException
    {
        berechneDistanzMatrix();
        int durchmesser = 0;

        for (int i = 1; i <= this.d.arrayLength(); i++)
        {
            if (durchmesser < this.d.maxVonZeile(i))
            {
                durchmesser = this.d.maxVonZeile(i);
            }
        }
        return durchmesser;
    }

    // Return: 1 steht fuer A, 2 fuer B, etc ...
    public List<Integer> berechneZentrum() throws MatrixException
    {
        berechneDistanzMatrix();

        List<Integer> zentrum = new ArrayList<>();
        int radius = berechneRadius();
        int[] exzArray = this.berechneExzentrizitaeten();

        for (int i = 0; i < this.a.arrayLength(); i++)
        {
            if (exzArray[i] == radius)
            {
                zentrum.add(i + 1);
            }
        }

        return zentrum;
    }

    public int berechneZusammenhangsKomponentenAnzahl() throws MatrixException
    {
        this.berechneWegMatrix();

        List<int[]> zsmhKomp = new ArrayList<>();

        for (int i = 0; i < this.w.arrayLength(); i++)
        {
            int[] zeileKomp = new int[this.w.arrayLength()];
            for (int j = 0; j < this.w.arrayLength(); j++)
            {
                zeileKomp[j] = this.w.getWert(i, j);
            }
            if (zsmhKomp.stream().noneMatch(a -> Arrays.equals(a, zeileKomp)))
            {
                zsmhKomp.add(zeileKomp);
            }
        }
        return zsmhKomp.size();
    }

    public ArrayList<ArrayList<Integer>> berechneZusammenhangsKomponenten() throws MatrixException
    {
        this.berechneWegMatrix();

        ArrayList<ArrayList<Integer>> zsmhKomp = new ArrayList<>();

        for (int i = 0; i < this.w.arrayLength(); i++)
        {
            ArrayList<Integer> zeileKomp = new ArrayList<>();

            for (int j = 0; j < this.w.arrayLength(); j++)
            {
                if (this.w.getWert(i, j) == 1)
                {
                    zeileKomp.add(j + 1);
                }
            }
            if (!zsmhKomp.contains(zeileKomp))
            {
                zsmhKomp.add(zeileKomp);
            }
        }

        return zsmhKomp;
    }

    // Return: 1 steht fuer A, 2 fuer B, etc ...
    public List<Integer> berechneArtikulationen() throws MatrixException
    {
        int zsmhKomp = berechneZusammenhangsKomponentenAnzahl();
        List<Integer> artikulationen = new ArrayList<>();

        for (int i = 0; i < this.a.arrayLength(); i++)
        {
            Matrix artMatrix = this.a.clone();

            for (int j = 0; j < this.a.arrayLength(); j++)
            {
                artMatrix.setWert(0, i, j);
                artMatrix.setWert(0, j, i);
            }
            Graph artGraph = new Graph(artMatrix);
            if (zsmhKomp < artGraph.berechneZusammenhangsKomponentenAnzahl() - 1)
            {
                artikulationen.add(i + 1);
            }
        }
        return artikulationen;
    }

    // Return: 1 steht fuer A, 2 fuer B, etc ... [1, 2] ist Bruecke zwischen A - B
    public List<ArrayList<Integer>> berechneBruecken() throws MatrixException
    {
        int zsmhKomp = berechneZusammenhangsKomponentenAnzahl();
        List<ArrayList<Integer>> bruecken = new ArrayList<>();

        for (int i = 0; i < this.a.arrayLength(); i++)
        {
            Matrix brueckenMatrix = this.a.clone();

            for (int j = 0; j < this.a.arrayLength(); j++)
            {
                if (brueckenMatrix.getWert(i, j) == 1)
                {
                    brueckenMatrix.setWert(0, i, j);
                    brueckenMatrix.setWert(0, j, i);

                    Graph brueckenGraph = new Graph(brueckenMatrix);

                    if (brueckenGraph.berechneZusammenhangsKomponentenAnzahl() > zsmhKomp)
                    {
                        ArrayList<Integer> bruecke = new ArrayList<>();
                        bruecke.add(Math.min(i + 1, j + 1));
                        bruecke.add(Math.max(i + 1, j + 1));

                        if (!bruecken.contains(bruecke))
                        {
                            bruecken.add(bruecke);
                        }
                    }
                    brueckenMatrix.setWert(1, i, j);
                    brueckenMatrix.setWert(1, j, i);
                }
            }
        }
        return bruecken;
    }

    // Return: 1 steht fuer A, 2 fuer B, etc ... [1, 2] ist ein Block
    public ArrayList<ArrayList<Integer>> berechneBloecke() throws MatrixException
    {
        List<Integer> artikulationen = this.berechneArtikulationen();
        List<ArrayList<Integer>> bruecken = this.berechneBruecken();

        // jede Bruecke ist ein Block
        ArrayList<ArrayList<Integer>> bloecke = new ArrayList<>(bruecken);

        // keine Artikulationen, Graph ist ein grosser Block
        if (artikulationen.isEmpty() && this.zusammenhaengend())
        {
            ArrayList<Integer> keineArtikulationen = new ArrayList<>();
            for (int i = 0; i < this.a.arrayLength(); i++)
            {
                keineArtikulationen.add(i + 1);
            }
            bloecke.add(keineArtikulationen);
        }

        // isolierte Knoten hinzufuegen
        int[] knotenGrade = this.a.knotenGrad();
        for (int i = 0; i < knotenGrade.length; i++)
        {
            if (knotenGrade[i] == 0)
            {
                ArrayList<Integer> isolierterKnoten = new ArrayList<>();
                isolierterKnoten.add(i + 1);
                bloecke.add(isolierterKnoten);
            }
        }

        // main
        Matrix removeKanten = this.a.clone();

        // bis Matrix keine Kanten mehr hat
        while (removeKanten.anzahlKantenGesamt() >= 1)
        {
            // groesste zusammenhaengende artikulationsfreie Teilgraph
            Matrix removeKantenUpdated = removeKanten.clone();
            Graph removeKantenGraph = new Graph(removeKantenUpdated);

            artikulationen = removeKantenGraph.berechneArtikulationen();

            // entfernen der Artikulationen
            for (Integer integer : artikulationen)
            {
                for (int j = 0; j < removeKantenUpdated.arrayLength(); j++)
                {
                    removeKantenUpdated.setWert(0, integer - 1, j); // -1, weil Artikulationen mit 0 = 1 angegeben werden
                    removeKantenUpdated.setWert(0, j, integer - 1);
                }
            }

            // Berechnung neuer Komponenten
            ArrayList<ArrayList<Integer>> zsmhKomp = removeKantenGraph.berechneZusammenhangsKomponenten();
            Iterator<ArrayList<Integer>> zsmhKompIterator = zsmhKomp.iterator();

            // entfernen aller Komponenten mit nur einem Knoten, Bruecken + isolierte bereits hinzugefuegt
            while (zsmhKompIterator.hasNext())
            {
                ArrayList<Integer> kompEnde = zsmhKompIterator.next();
                if (kompEnde.size() == 1)
                {
                    zsmhKompIterator.remove();
                }
            }

            // Iterator ohne Isolierte
            Iterator<ArrayList<Integer>> zsmhKompIteratorUpdated = zsmhKomp.iterator();

            // bis keine Komponenten im artikulationsfreien Graphen
            while (zsmhKompIteratorUpdated.hasNext())
            {
                ArrayList<Integer> block = zsmhKompIteratorUpdated.next();

                // fuer jeden Knoten schauen, ob er mit einer Artikulation verbunden ist
                for (int i = 0; i < block.size(); i++)
                {
                    Integer knoten = block.get(i);

                    // wenn Knoten keine Artikulation ist
                    if (!artikulationen.contains(knoten))
                    {
                        for (int j = 0; j < artikulationen.size(); j++)
                        {
                            Integer artikulation = artikulationen.get(j);
                            if (this.a.getWert(knoten - 1, artikulation - 1) == 1)
                            {
                                if (!block.contains(artikulation)) // nicht - 1!
                                {
                                    block.add(artikulation);
                                }
                                if (!bloecke.contains(block))
                                {
                                    if (block.size() > 2) // Bruecke schon hinzugefuegt
                                    {
                                        block.sort(Integer::compare);
                                        bloecke.add(block);
                                    }
                                }
                            }
                        }

                        // falls in if-Bedingung in 377 nicht reingekommen wird
                        if (!bloecke.contains(block))
                        {
                            if (block.size() > 2)
                            {
                                block.sort(Integer::compare);
                                bloecke.add(block);
                            }
                        }
                    }
                }
            }
            // Graph auf neuen Stand von Matrix updaten
            removeKantenGraph = new Graph(removeKanten);
            // Kanten + Knoten entfernen, die jetzige Bloecke beinhalten
            removeKanten = entferneBlock((ArrayList<Integer>) removeKantenGraph.berechneArtikulationen(), bloecke, removeKanten);
            // berechneArtikulationen() liefert List<> statt ArrayList<> zurueck
        }

        return bloecke;
    }

    // entfernt alle Knoten, die einem Block angehören, außer Artikulationen
    private Matrix entferneBlock(ArrayList<Integer> artikulationen, ArrayList<ArrayList<Integer>> bloecke, Matrix matrix) throws MatrixException
    {
        Matrix removed = matrix.clone();

        ArrayList<Integer> brueckenKnoten = new ArrayList<>();
        List<ArrayList<Integer>> bruecken = this.berechneBruecken();

        // alle Knoten, die einer Bruecke angehoeren werden hinzugefuegt
        for (ArrayList<Integer> integers : bruecken)
        {
            brueckenKnoten.addAll(integers);
        }

        Iterator<ArrayList<Integer>> bloeckeIterator = bloecke.iterator();
        while (bloeckeIterator.hasNext())
        {
            ArrayList<Integer> block = bloeckeIterator.next();

            for (int i = 0; i < block.size(); i++)
            {
                Integer knoten = block.get(i);

                if (!artikulationen.contains(knoten))
                {
                    if (brueckenKnoten.contains(knoten))
                    {
                        int[] knotenGrade = removed.knotenGrad();
                        if (knotenGrade[knoten - 1] == 1)
                        {
                            for (int j = 0; j < removed.arrayLength(); j++)
                            {
                                removed.setWert(0, knoten - 1, j);
                                removed.setWert(0, j, knoten - 1);
                            }
                        }
                    }
                    if (!brueckenKnoten.contains(knoten))
                    {
                        for (int j = 0; j < removed.arrayLength(); j++)
                        {
                            removed.setWert(0, knoten - 1, j);
                            removed.setWert(0, j, knoten - 1);
                        }
                    }
                }
            }
        }
        return removed;
    }

    private boolean zusammenhaengend() throws MatrixException
    {
        this.berechneWegMatrix();

        for (int i = 0; i < this.w.arrayLength(); i++)
        {
            for (int j = 0; j < this.w.arrayLength(); j++)
            {
                if (this.w.getWert(i, j) != 1)
                {
                    return false;
                }
            }
        }

        return true;
    }


    // Import/Export

    public void exportMatrixCsv(String filename) throws MatrixException
    {
        if (!checkFilename(filename))
        {
            throw new MatrixException("invalid filename");
        }

        try (BufferedWriter bw = Files.newBufferedWriter(
                Paths.get(System.getProperty("user.dir"), filename),
                StandardCharsets.ISO_8859_1,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
        ))
        {
            bw.write(this.a.toStringCsv());
        }
        catch (IOException e)
        {
            throw new MatrixException("Fehler: ", e);
        }
    }

    public Matrix importMatrixCsv(String filename) throws MatrixException
    {
        if (!checkFilename(filename))
        {
            throw new MatrixException("invalid filename");
        }

        try (BufferedReader br = Files.newBufferedReader(
                Paths.get(System.getProperty("user.dir"), filename),
                StandardCharsets.ISO_8859_1
        ))
        {

            String line = br.readLine();
            Matrix importedMatrix = new Matrix(line.split(File.pathSeparator).length);
            int help = 0;

            while (line != null)
            {
                String[] values = line.split(File.pathSeparator);

                for (int i = 0; i < values.length; i++)
                {
                    importedMatrix.setWert(Integer.parseInt(values[i]), help, i);
                }
                line = br.readLine();
                help++;
            }

            return importedMatrix;
        }
        catch (IOException e)
        {
            throw new MatrixException("Fehler: ", e);
        }
    }

    public boolean checkFilename(String filename) throws MatrixException
    {
        if (filename == null)
        {
            throw new MatrixException("Error: filename can't be null");
        }
        if (!filename.endsWith(".csv"))
        {
            throw new MatrixException("Error: filename must end with .csv");
        }
        return true;
    }
}
