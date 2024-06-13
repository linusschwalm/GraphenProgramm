package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest
{
    private Matrix matrix;
    private Matrix adjMatrix;
    private Graph graph;
    private Graph adjGraph;
    private Graph bigAdjGraph;

    @BeforeEach
    void setUp() throws MatrixException
    {
        int[][] tmpA =
                {
                    {1, 2, 3},
                    {3, 4, 5},
                    {1, 0, 4}
                };
        matrix = new Matrix(tmpA);
        graph = new Graph(matrix);

        int[][] tmpB =
                {
                        {0, 1, 1, 1, 0},
                        {1, 0, 0, 1, 1},
                        {1, 0, 0, 1, 0},
                        {1, 1, 1, 0, 0},
                        {0, 1, 0, 0, 0}
                };
        adjMatrix  = new Matrix(tmpB);
        adjGraph = new Graph(adjMatrix);

        bigAdjGraph = new Graph("bigGraph.csv");
    }

    @Test
    void berechneAdjazenzmatrix()
    {
        try
        {
            graph.berechneAdjazenzmatrix("test.csv");
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechnePotenzMatrix()
    {
        try
        {
            int[][] tmpPotenz =
                {
                        {65, 60, 180},
                        {135, 128, 366},
                        {30, 18, 101}
                };
            Matrix tmpMatrix = new Matrix(tmpPotenz);

            graph.berechnePotenzMatrix(3);

            assertEquals(tmpMatrix, graph.p);
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void potenzMatrixTest() throws MatrixException
    {
        int[][] test =
                {
                        {1, 0, 1},
                        {0, 2, 0},
                        {1, 0, 1}
                };
        Matrix testedMatrix = new Matrix(test);


        int[][] tmpPotenz =
                {
                        {0, 1, 0},
                        {1, 0, 1},
                        {0, 1, 0}
                };
        Matrix matrix1 = new Matrix(tmpPotenz);
        Graph graph1 = new Graph(matrix1);


        graph1.berechnePotenzMatrix(2);
        graph1.p.print();
        assertEquals(testedMatrix, graph1.p);
    }

    @Test
    void berechneDistanzMatrix()
    {
        try
        {
            adjGraph.berechneDistanzMatrix();

            int[][] equaledBigDistance =
                    {
                            {0, 1, 1, 1, 2},
                            {1, 0, 2, 1, 1},
                            {1, 2, 0, 1, 3},
                            {1, 1, 1, 0, 2},
                            {2, 1, 3, 2, 0}
                    };
            Matrix equaledMatrix = new Matrix(equaledBigDistance);
            assertEquals(equaledMatrix, adjGraph.d);
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneWegMatrix()
    {
        try
        {
            int[][] tmpBigMatrix =
                    {
                            {0, 0, 1, 1, 0},
                            {0, 0, 0, 0, 1},
                            {1, 0, 0, 0, 0},
                            {1, 0, 0, 0, 0},
                            {0, 1, 0, 0, 0}
                    };
            Matrix bigMatrix = new Matrix(tmpBigMatrix);
            Graph tmpGraph = new Graph(bigMatrix);
            tmpGraph.berechneWegMatrix();

            int[][] equaledBigWay =
                    {
                            {1, 0, 1, 1, 0},
                            {0, 1, 0, 0, 1},
                            {1, 0, 1, 1, 0},
                            {1, 0, 1, 1, 0},
                            {0, 1, 0, 0, 1}
                    };
            Matrix equaledMatrix = new Matrix(equaledBigWay);
            assertEquals(equaledMatrix, tmpGraph.w);
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneExzentrizitaeten()
    {
        int[] exzentrizitaetenTest = new int[5];
        exzentrizitaetenTest[0] = 2;
        exzentrizitaetenTest[1] = 2;
        exzentrizitaetenTest[2] = 3;
        exzentrizitaetenTest[3] = 2;
        exzentrizitaetenTest[4] = 3;
        try
        {
            int[] exz = adjGraph.berechneExzentrizitaeten();
            for (int i = 0; i < exzentrizitaetenTest.length; i++)
            {
                assertEquals(exzentrizitaetenTest[i], exz[i]);
            }
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
        StringBuilder vergleich = new StringBuilder();
        vergleich.append("Exzentrizitaet von 1: 2").append("\n");
        vergleich.append("Exzentrizitaet von 2: 2").append("\n");
        vergleich.append("Exzentrizitaet von 3: 3").append("\n");
        vergleich.append("Exzentrizitaet von 4: 2").append("\n");
        vergleich.append("Exzentrizitaet von 5: 3").append("\n");
    }

    @Test
    void berechneRadius()
    {
        int[][] radiusTest =
                {
                        {0, 1, 0, 1, 0, 1},
                        {1, 0, 1, 0, 0, 0},
                        {0, 1, 0, 1, 0, 0},
                        {1, 0, 1, 0, 1, 0},
                        {0, 0, 0, 1, 0, 1},
                        {1, 0, 0, 0, 1, 0},
                };
        Matrix radiusMatrix = new Matrix(radiusTest);
        Graph radiusGraph = new Graph(radiusMatrix);
        try
        {
            assertEquals(2, radiusGraph.berechneRadius());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneDurchmesser()
    {
        try
        {
            assertEquals(3, adjGraph.berechneDurchmesser());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneZentrum()
    {
        int[][] zentrumTest =
                {
                        {0, 1, 0, 1, 0, 1},
                        {1, 0, 1, 0, 0, 0},
                        {0, 1, 0, 1, 0, 0},
                        {1, 0, 1, 0, 1, 0},
                        {0, 0, 0, 1, 0, 1},
                        {1, 0, 0, 0, 1, 0},
                };
        Matrix zentrumMatrix = new Matrix(zentrumTest);
        Graph zentrumGraph = new Graph(zentrumMatrix);
        try
        {
            List<Integer> zentrum = new ArrayList<>();
            zentrum.add(1);
            zentrum.add(4);
            assertEquals(zentrumGraph.berechneZentrum(), zentrum);
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneZusammenhangsKomponentenAnzahl()
    {
        int[][] zsmhTest =
                {
                        {0, 0, 0, 1, 0, 0},
                        {0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 1, 0, 0},
                        {1, 0, 1, 0, 0, 0},
                        {0, 0, 0, 0, 0, 1},
                        {0, 0, 0, 0, 1, 0}
                };
        Matrix zsmhMatrix = new Matrix(zsmhTest);
        Graph zsmhGraph = new Graph(zsmhMatrix);

        try
        {
            assertEquals(3, zsmhGraph.berechneZusammenhangsKomponentenAnzahl());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneZusammenhangsKomponentenAnzahl2()
    {
        int[][] zsmhTest =
                {
                        {0, 0, 1, 1, 0},
                        {0, 0, 0, 0, 1},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0}
                };
        Matrix zsmhMatrix = new Matrix(zsmhTest);
        Graph zsmhGraph = new Graph(zsmhMatrix);

        try
        {
            assertEquals(2, zsmhGraph.berechneZusammenhangsKomponentenAnzahl());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneZusammenhangsKomponenten()
    {
        int[][] zsmhTest =
                {
                        {0, 0, 1, 1, 0},
                        {0, 0, 0, 0, 1},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0}
                };
        Matrix zsmhMatrix = new Matrix(zsmhTest);
        Graph zsmhGraph = new Graph(zsmhMatrix);

        try
        {
            ArrayList<ArrayList<Integer>> zsmhKomp = new ArrayList<>();
            ArrayList<Integer> innerZsmhKomp = new ArrayList<>();
            ArrayList<Integer> secondInnerZsmhKomp = new ArrayList<>();
            innerZsmhKomp.add(1);
            innerZsmhKomp.add(3);
            innerZsmhKomp.add(4);
            secondInnerZsmhKomp.add(2);
            secondInnerZsmhKomp.add(5);
            zsmhKomp.add(innerZsmhKomp);
            zsmhKomp.add(secondInnerZsmhKomp);

            assertEquals(zsmhKomp, zsmhGraph.berechneZusammenhangsKomponenten());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneArtikulationen()
    {
        int[][] artTest =
                {
                        {0, 1, 1, 0, 1, 0, 0, 0},
                        {1, 0, 1, 1, 0, 0, 0, 0},
                        {1, 1, 0, 1, 1, 0, 0, 0},
                        {0, 1, 1, 0, 1, 0, 0, 0},
                        {1, 0, 1, 1, 0, 1, 1, 0},
                        {0, 0, 0, 0, 1, 0, 1, 1},
                        {0, 0, 0, 0, 1, 1, 0, 1},
                        {0, 0, 0, 0, 0, 1, 1, 0}
                };
        Matrix artMatrix = new Matrix(artTest);
        Graph artGraph = new Graph(artMatrix);

        try
        {
            List<Integer> artiks = new ArrayList<>();
            artiks.add(5);
            assertEquals(artiks, artGraph.berechneArtikulationen());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneBruecken()
    {
        int[][] brueckenTest =
                {
                        {0, 1, 1, 0},
                        {1, 0, 1, 0},
                        {1, 1, 0, 1},
                        {0, 0, 1, 0}
                };
        Matrix brueckenMatrix = new Matrix(brueckenTest);
        Graph brueckenGraph = new Graph(brueckenMatrix);

        try
        {
            List<ArrayList<Integer>> bruecken = new ArrayList<>();
            ArrayList<Integer> innerBruecke = new ArrayList<>();
            innerBruecke.add(3);
            innerBruecke.add(4);
            bruecken.add(innerBruecke);
            assertEquals(bruecken, brueckenGraph.berechneBruecken());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneBloecke()
    {
        int[][] bloeckeTest =
                {
                        {0, 1, 1, 0, 1, 0, 0, 0},
                        {1, 0, 1, 1, 0, 0, 0, 0},
                        {1, 1, 0, 1, 1, 0, 0, 0},
                        {0, 1, 1, 0, 1, 0, 0, 0},
                        {1, 0, 1, 1, 0, 1, 1, 0},
                        {0, 0, 0, 0, 1, 0, 1, 1},
                        {0, 0, 0, 0, 1, 1, 0, 1},
                        {0, 0, 0, 0, 0, 1, 1, 0}
                };
        Matrix bloeckeMatrix = new Matrix(bloeckeTest);
        Graph bloeckeGraph = new Graph(bloeckeMatrix);

        try
        {
            ArrayList<ArrayList<Integer>> bloecke = getArrayLists();
            assertEquals(bloecke, bloeckeGraph.berechneBloecke());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    private static ArrayList<ArrayList<Integer>> getArrayLists()
    {
        ArrayList<ArrayList<Integer>> bloecke = new ArrayList<>();
        ArrayList<Integer> innerBloecke = new ArrayList<>();
        ArrayList<Integer> innerBloecke2 = new ArrayList<>();
        innerBloecke.add(1);
        innerBloecke.add(2);
        innerBloecke.add(3);
        innerBloecke.add(4);
        innerBloecke.add(5);
        innerBloecke2.add(5);
        innerBloecke2.add(6);
        innerBloecke2.add(7);
        innerBloecke2.add(8);
        bloecke.add(innerBloecke);
        bloecke.add(innerBloecke2);
        return bloecke;
    }

    @Test
    void berechneBloeckeNurBruecken()
    {
        int[][] bloeckeTest =
                {
                        {0, 1, 0, 0},
                        {1, 0, 1, 0},
                        {0, 1, 0, 1},
                        {0, 0, 1, 0}
                };
        Matrix bloeckeMatrix = new Matrix(bloeckeTest);
        Graph bloeckeGraph = new Graph(bloeckeMatrix);

        ArrayList<ArrayList<Integer>> bloecke = getLists();

        try
        {
            assertEquals(bloecke, bloeckeGraph.berechneBloecke());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    private static ArrayList<ArrayList<Integer>> getLists()
    {
        ArrayList<ArrayList<Integer>> bloecke = new ArrayList<>();
        ArrayList<Integer> innerBloecke = new ArrayList<>();
        ArrayList<Integer> innerBloecke2 = new ArrayList<>();
        ArrayList<Integer> innerBloecke3 = new ArrayList<>();
        innerBloecke.add(1);
        innerBloecke.add(2);
        innerBloecke2.add(2);
        innerBloecke2.add(3);
        innerBloecke3.add(3);
        innerBloecke3.add(4);
        bloecke.add(innerBloecke);
        bloecke.add(innerBloecke2);
        bloecke.add(innerBloecke3);
        return bloecke;
    }

    @Test
    void exportMatrixCsv()
    {
        try
        {
            graph.exportMatrixCsv("test.csv");
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void importMatrixCsv()
    {
        try
        {
            Matrix a = graph.importMatrixCsv("test.csv");
            assertEquals(matrix, a);
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void checkFilenameNull()
    {
        MatrixException exception = Assertions.assertThrows(MatrixException.class, () -> graph.checkFilename(null));
        assertEquals("Error: filename can't be null", exception.getMessage());
    }

    @Test
    void checkFilenameEndsWithSer()
    {
        MatrixException exception = Assertions.assertThrows(MatrixException.class, () -> graph.checkFilename("test.ser"));
        assertEquals("Error: filename must end with .csv", exception.getMessage());
    }

    @Test
    void checkFilenameHappyPath()
    {
        try
        {
            assertTrue(graph.checkFilename("test.csv"));
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }


    // Tests mit grossem Graph
    @Test
    void berechnePotenzMatrixBig()
    {
        try
        {
            Graph testGraph = new Graph("bigGraphPotenz4.csv");
            bigAdjGraph.berechnePotenzMatrix(4);
            assertEquals(testGraph.a, bigAdjGraph.p);
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneDistanzMatrixBig()
    {
        try
        {
            Graph testGraph = new Graph("bigDistance.csv");
            bigAdjGraph.berechneDistanzMatrix();

            assertEquals(testGraph.a, bigAdjGraph.d);
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneWegMatrixBig()
    {
        try
        {
            Graph testGraph = new Graph("bigWeg.csv");
            bigAdjGraph.berechneWegMatrix();

            assertEquals(testGraph.a, bigAdjGraph.w);
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneExzentrizitaetenBig()
    {
        int[] exzentrizitaetenTest = {9, 8, 7, 7, 6, 7, 7, 5, 6, 5, 5, 8, 9, 6, 7, 7, 6, 8, 8, 7, 7, 8, 9, 9};

        try
        {
            int[] exz = bigAdjGraph.berechneExzentrizitaeten();
            for (int i = 0; i < exzentrizitaetenTest.length; i++)
            {
                assertEquals(exzentrizitaetenTest[i], exz[i]);
            }
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneRadiusBig()
    {
        try
        {
            assertEquals(5, bigAdjGraph.berechneRadius());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneDurchmesserBig()
    {
        try
        {
            assertEquals(9, bigAdjGraph.berechneDurchmesser());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneZentrumBig()
    {
        List<Integer> zentrum = new ArrayList<>();
        zentrum.add(8);
        zentrum.add(10);
        zentrum.add(11);

        try
        {
            assertEquals(bigAdjGraph.berechneZentrum(), zentrum);
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneZusammenhangsKomponentenAnzahlBig()
    {
        try
        {
            assertEquals(1, bigAdjGraph.berechneZusammenhangsKomponentenAnzahl());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneZusammenhangsKomponentenBig()
    {

        try
        {
            // Ausgabe sind einfach alle beinhaltenden Knoten
            System.out.println(bigAdjGraph.berechneZusammenhangsKomponenten());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneArtikulationenBig()
    {
        List<Integer> artikulationen = new ArrayList<>();
        artikulationen.add(2);
        artikulationen.add(5);
        artikulationen.add(7);
        artikulationen.add(8);
        artikulationen.add(11);
        artikulationen.add(12);
        artikulationen.add(15);
        artikulationen.add(17);
        artikulationen.add(21);
        artikulationen.add(22);

        try
        {
            assertEquals(artikulationen, bigAdjGraph.berechneArtikulationen());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneBrueckenBig()
    {
        List<ArrayList<Integer>> bruecken = new ArrayList<>();
        ArrayList<Integer> innerBruecke = new ArrayList<>();
        ArrayList<Integer> innerBruecke2 = new ArrayList<>();
        ArrayList<Integer> innerBruecke3 = new ArrayList<>();
        ArrayList<Integer> innerBruecke4 = new ArrayList<>();
        innerBruecke.add(1);
        innerBruecke.add(2);
        innerBruecke2.add(7);
        innerBruecke2.add(12);
        innerBruecke3.add(12);
        innerBruecke3.add(13);
        innerBruecke4.add(21);
        innerBruecke4.add(22);
        bruecken.add(innerBruecke);
        bruecken.add(innerBruecke2);
        bruecken.add(innerBruecke3);
        bruecken.add(innerBruecke4);

        try
        {
            assertEquals(bruecken, bigAdjGraph.berechneBruecken());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    @Test
    void berechneBloeckeBig()
    {
        ArrayList<ArrayList<Integer>> bloecke = getBigGraphBloecke();

        try
        {
            assertEquals(bloecke, bigAdjGraph.berechneBloecke());
        }
        catch (Exception e)
        {
            fail("There shouldn't be an Exception");
        }
    }

    private static ArrayList<ArrayList<Integer>> getBigGraphBloecke()
    {
        ArrayList<ArrayList<Integer>> bloecke = new ArrayList<>();
        ArrayList<Integer> innerBloecke = new ArrayList<>();
        ArrayList<Integer> innerBloecke2 = new ArrayList<>();
        ArrayList<Integer> innerBloecke3 = new ArrayList<>();
        ArrayList<Integer> innerBloecke4 = new ArrayList<>();
        ArrayList<Integer> innerBloecke5 = new ArrayList<>();
        ArrayList<Integer> innerBloecke6 = new ArrayList<>();
        ArrayList<Integer> innerBloecke7 = new ArrayList<>();
        ArrayList<Integer> innerBloecke8 = new ArrayList<>();
        ArrayList<Integer> innerBloecke9 = new ArrayList<>();
        ArrayList<Integer> innerBloecke10 = new ArrayList<>();
        ArrayList<Integer> innerBloecke11 = new ArrayList<>();
        ArrayList<Integer> innerBloecke12 = new ArrayList<>();
        innerBloecke.add(1);
        innerBloecke.add(2);
        innerBloecke2.add(7);
        innerBloecke2.add(12);
        innerBloecke3.add(12);
        innerBloecke3.add(13);
        innerBloecke4.add(5);
        innerBloecke4.add(6);
        innerBloecke4.add(7);
        innerBloecke5.add(22);
        innerBloecke5.add(23);
        innerBloecke5.add(24);
        innerBloecke6.add(21);
        innerBloecke6.add(22);
        innerBloecke7.add(17);
        innerBloecke7.add(20);
        innerBloecke7.add(21);
        innerBloecke8.add(15);
        innerBloecke8.add(18);
        innerBloecke8.add(19);
        innerBloecke9.add(11);
        innerBloecke9.add(14);
        innerBloecke9.add(15);
        innerBloecke9.add(16);
        innerBloecke9.add(17);
        innerBloecke10.add(8);
        innerBloecke10.add(10);
        innerBloecke10.add(11);
        innerBloecke11.add(5);
        innerBloecke11.add(8);
        innerBloecke11.add(9);
        innerBloecke12.add(2);
        innerBloecke12.add(3);
        innerBloecke12.add(4);
        innerBloecke12.add(5);
        bloecke.add(innerBloecke);
        bloecke.add(innerBloecke2);
        bloecke.add(innerBloecke3);
        bloecke.add(innerBloecke6);
        bloecke.add(innerBloecke12);
        bloecke.add(innerBloecke8);
        bloecke.add(innerBloecke5);
        bloecke.add(innerBloecke9);
        bloecke.add(innerBloecke4);
        bloecke.add(innerBloecke7);
        bloecke.add(innerBloecke11);
        bloecke.add(innerBloecke10);
        return bloecke;
    }

    @Test
    void starGraph()
    {
        try
        {
            Graph graph = new Graph("starGraph.csv");
            System.out.println(graph.berechneBloecke());
        }
        catch (MatrixException e)
        {
            throw new RuntimeException(e);
        }
    }
}
