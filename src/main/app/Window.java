package main.app;

import main.fill.ScanLine;
import main.fill.SeedFill;
import main.geometry.Point;
import main.image.Img;
import main.image.Pattern;
import main.render.Render;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Window
    {
        private static final int WIDTH = 1920;
        private static final int HEIGHT = 1000;
        private static final int IMG_WIDTH = 1500;
        private BufferedImage img;
        private Render render;
        private boolean activeIrr = false;
        private boolean clipActive = false;
        private boolean antiAliasing = false;
        private List<Point> points = new ArrayList<>();
        private List<Point> clipPoints = new ArrayList<>();
        private JLabel statusBar;
        private JColorChooser colorChooser;
        private JPanel colorPanel;
        private JRadioButton seedFillChoose;
        private JRadioButton scanLineChoose;
        private JCheckBox patternBox;
        private JCheckBox imageBox;
        private JCheckBox solidBox;
        private boolean scanLineOn = false;
        private boolean seedFillOn = true;
        private ScanLine scanLine;
        private SeedFill seedFill;
        private Img bImg;
        private Pattern pattern;
        private boolean patternOn = false;
        private boolean imageOn = false;
        private int color = 0xFFFFFF;
        private JFileChooser jFileChooser;


        private Window()
            {
                JFrame window = new JFrame();
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                window.setSize(WIDTH, HEIGHT);
                window.setTitle("PRGF Ukol 1");
                window.setLocationRelativeTo(null);
                img = new BufferedImage(IMG_WIDTH, window.getHeight(), BufferedImage.TYPE_INT_RGB);
                Canvas canvas = new Canvas();
                render = Render.getInstance();
                render.inicialize(img, canvas);
                window.add(canvas);
                window.setVisible(true);
                window.setResizable(false);
                statusBar = new JLabel();
                statusBar.setOpaque(false);
                statusBar.setText("Antialiasing: false  Press SPACE to switch RMB for Clipping Polynome, LMB for Polynome");
                window.add(statusBar, BorderLayout.SOUTH);

                JPanel controlPanel = new JPanel();
                JPanel spacer = new JPanel();
                spacer.setPreferredSize(new Dimension(420, 1));
                controlPanel.setPreferredSize(new Dimension(420, 1000));
                seedFillChoose = new JRadioButton("Seed fill");
                seedFillChoose.setSelected(true);
                scanLineChoose = new JRadioButton("Scan Line");
                colorChooser = new JColorChooser();
                AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
                for (AbstractColorChooserPanel panel : panels)
                    {
                        if (!panel.getDisplayName().equals("Swatches"))
                            {
                                colorChooser.removeChooserPanel(panel);
                            }
                    }
                colorChooser.setPreviewPanel(new JPanel());

                patternBox = new JCheckBox("fill with Pattern");
                imageBox = new JCheckBox("fill with Image");
                solidBox = new JCheckBox("Solid Color");

                solidBox.setSelected(true);
                imageBox.setVisible(false);

                JButton buttonImage = new JButton("Upload Image");


                ButtonGroup fill = new ButtonGroup();
                fill.add(scanLineChoose);
                fill.add(seedFillChoose);
                ButtonGroup pattimage = new ButtonGroup();
                pattimage.add(patternBox);
                pattimage.add(imageBox);
                pattimage.add(solidBox);
                controlPanel.add(seedFillChoose);
                controlPanel.add(scanLineChoose); //PROSIM JA CHCI VISUAL STUDIO DELAT GUI V TOMHLE JE TREST
                controlPanel.add(colorChooser);
                colorPanel = new JPanel();
                colorPanel.setPreferredSize(new Dimension(420, 25));
                colorPanel.add(new JLabel("Current Color"), BorderLayout.CENTER);
                colorPanel.setBackground(new Color(color));

                pattern = new Pattern();
                pattern.setLengthX(6);
                pattern.setLengthY(6);
                pattern.getImageFromPattern();

                bImg = new Img();

                JButton buttonClip = new JButton("Clip Polygon");

                JButton buttonDelete = new JButton("Delete Points");

                JButton buttonPause = new JButton("Pause Drawing");

                controlPanel.add(colorPanel);
                controlPanel.add(solidBox);
                controlPanel.add(patternBox);
                controlPanel.add(imageBox);
                controlPanel.add(spacer);
                controlPanel.add(buttonImage);
                controlPanel.add(buttonClip);
                controlPanel.add(buttonDelete);
                controlPanel.add(buttonPause);
                colorChooser.setColor(Color.WHITE);
                colorChooser.getSelectionModel().addChangeListener(changeEvent ->
                {
                    color = colorChooser.getColor().getRGB();
                    colorPanel.setBackground(colorChooser.getColor());
                    drawDonePolygon();
                });

                patternBox.addActionListener(actionEvent ->
                {
                    if (patternBox.isSelected())
                        {
                            patternOn = true;
                            imageOn = false;

                        }

                });

                imageBox.addActionListener(actionEvent ->
                {
                    if (imageBox.isSelected())
                        {
                            imageOn = true;
                            patternOn = false;
                        }

                });

                solidBox.addActionListener(actionEvent ->
                {
                    if (solidBox.isSelected())
                        {
                            patternOn = false;
                            imageOn = false;
                        }
                });

                seedFillChoose.addActionListener(actionEvent ->
                {
                    if (seedFillChoose.isSelected())
                        {
                            seedFillOn = true;
                            scanLineOn = false;
                        }
                    else
                        {
                            seedFillOn = false;
                            scanLineOn = true;
                        }
                });

                scanLineChoose.addActionListener(actionEvent ->
                {
                    if (!scanLineChoose.isSelected())
                        {
                            seedFillOn = true;
                            scanLineOn = false;
                        }
                    else
                        {
                            seedFillOn = false;
                            scanLineOn = true;
                        }
                });

                buttonImage.addActionListener(actionEvent ->
                {
                    jFileChooser = new JFileChooser();
                    jFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                    if (jFileChooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION)
                        {
                            File imageFile = jFileChooser.getSelectedFile();
                            try
                                {
                                    if (imageFile.getCanonicalPath().contains(".png"))
                                        {
                                            bImg.setbImage(ImageIO.read(imageFile));
                                            imageBox.setVisible(true);
                                        }
                                    else
                                        {
                                            JOptionPane.showMessageDialog(new JFrame(), "Accepts only .png images");
                                        }

                                }
                            catch (IOException e)
                                {
                                    JOptionPane.showMessageDialog(new JFrame(), "Bad File Input");
                                }
                        }


                });

                buttonClip.addActionListener(actionEvent ->
                {
                    clipActive = false;
                    List<Point> newPoints;
                    newPoints = render.clip(points, clipPoints);
                    render.clear();
                    render.drawPolygon(newPoints, antiAliasing, 0, 0xFF0000);

                    clipPoints.clear();
                });


                buttonDelete.addActionListener(actionEvent ->
                {
                    render.clear();
                    points = new ArrayList<>();
                    clipPoints = new ArrayList<>();
                    activeIrr = false;
                    clipActive = false;

                });

                buttonPause.addActionListener(actionEvent ->
                {

                });
                window.add(controlPanel, BorderLayout.EAST);
                canvas.addMouseListener(new MouseAdapter()
                    {
                        @Override
                        public void mouseClicked(MouseEvent mouseEvent)
                            {
                                if (mouseEvent.isControlDown())
                                    {
                                        if (scanLineOn && !points.isEmpty())
                                            {
                                                if (imageOn)
                                                    scanLine = new ScanLine(points, render, color, true, bImg);
                                                else if (patternOn)
                                                    scanLine = new ScanLine(points, render, color, true, pattern);
                                                else
                                                    scanLine = new ScanLine(points, render, color, false, null);


                                                scanLine.init();
                                            }
                                        if (seedFillOn)
                                            {
                                                seedFill = new SeedFill();
                                                seedFill.setBufferedImage(img);
                                                if (imageOn)
                                                    seedFill.init(mouseEvent.getX(), mouseEvent.getY(), color, imageOn, bImg);
                                                else if (patternOn)
                                                    seedFill.init(mouseEvent.getX(), mouseEvent.getY(), color, patternOn, pattern);
                                                else
                                                    seedFill.init(mouseEvent.getX(), mouseEvent.getY(), color, false, null);


                                            }
                                        drawDonePolygon();
                                        return;
                                    }
                                if (SwingUtilities.isLeftMouseButton(mouseEvent))
                                    {
                                        render.clear();
                                        Point p = new Point(mouseEvent.getX(), mouseEvent.getY());
                                        points.add(p);
                                        render.drawPolygon(points, antiAliasing, 0, color);
                                        render.drawPixel((int) p.getX(), (int) p.getY(), color);
                                        if (points.size() > 2)
                                            {
                                                if (antiAliasing)
                                                    render.drawXiaolinWuLine(points.get(0), points.get(points.size() - 1), 0);
                                                else
                                                    render.drawLine(points.get(0), points.get(points.size() - 1), color, false, null);
                                            }
                                        activeIrr = true;
                                        drawDonePolygon();
                                    }

                                if (SwingUtilities.isRightMouseButton(mouseEvent))
                                    {
                                        render.clear();
                                        Point p = new Point(mouseEvent.getX(), mouseEvent.getY());
                                        clipPoints.add(p);
                                        render.drawPolygon(clipPoints, antiAliasing, 0, 0xFF0000);
                                        render.drawPixel((int) p.getX(), (int) p.getY(), 0xFF0000);
                                        if (clipPoints.size() > 2)
                                            {
                                                if (antiAliasing)
                                                    render.drawXiaolinWuLine(clipPoints.get(0), clipPoints.get(points.size() - 1), 0);
                                                else
                                                    render.drawLine(clipPoints.get(0), clipPoints.get(clipPoints.size() - 1), 0xFF0000, false, null);
                                            }
                                        clipActive = true;
                                        drawDonePolygon();
                                    }


                            }
                    });
                canvas.addMouseMotionListener(new MouseAdapter()
                    {
                        @Override
                        public void mouseMoved(MouseEvent e)
                            {
                                if (activeIrr)
                                    {
                                        render.clear();
                                        drawDonePolygon();
                                        if (antiAliasing)
                                            {
                                                render.drawXiaolinWuLine(new Point(e.getX(), e.getY()), points.get(points.size() - 1), 1);
                                                render.drawXiaolinWuLine(new Point(e.getX(), e.getY()), points.get(0), 1);
                                            }
                                        else
                                            {
                                                render.drawLine(new Point(e.getX(), e.getY()), points.get(points.size() - 1), 0xFF0000, false, null);
                                                render.drawLine(new Point(e.getX(), e.getY()), points.get(0), 0xFF0000, false, null);
                                            }
                                    }

                                if (clipActive)
                                    {
                                        render.clear();
                                        drawDonePolygon();
                                        if (antiAliasing)
                                            {
                                                render.drawXiaolinWuLine(new Point(e.getX(), e.getY()), clipPoints.get(clipPoints.size() - 1), 1);
                                                render.drawXiaolinWuLine(new Point(e.getX(), e.getY()), clipPoints.get(0), 1);
                                            }
                                        else
                                            {
                                                render.drawLine(new Point(e.getX(), e.getY()), clipPoints.get(clipPoints.size() - 1), 0xFF0000, false, null);
                                                render.drawLine(new Point(e.getX(), e.getY()), clipPoints.get(0), 0xFF0000, false, null);
                                            }
                                    }

                            }
                    });
                canvas.addKeyListener(new KeyAdapter()
                    {
                        @Override
                        public void keyPressed(KeyEvent e)
                            {

                                if (e.getKeyCode() == KeyEvent.VK_SPACE)
                                    {
                                        antiAliasing = !antiAliasing;
                                        render.clear();
                                        drawDonePolygon();
                                        statusBar.setText("Antialiasing: " + antiAliasing + " Press SPACE to switch RMB for Clipping Polynome, LMB for Polynome");
                                    }
                                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                                    {
                                        pause();
                                    }
                            }
                    });
            }

        public static void main(String[] args)
            {
                SwingUtilities.invokeLater(Window::new);
            }

        private void drawDonePolygon()
            {
                render.drawPolygon(points, antiAliasing, 0, color);
                if (points.size() > 2)
                    {
                        if (antiAliasing)
                            render.drawXiaolinWuLine(points.get(0), points.get(points.size() - 1), 0);
                        else
                            render.drawLine(points.get(0), points.get(points.size() - 1), color, false, null);
                    }

                render.drawPolygon(clipPoints, antiAliasing, 0, 0xFF0000);
                if (clipPoints.size() > 2)
                    {
                        if (antiAliasing)
                            render.drawXiaolinWuLine(clipPoints.get(0), clipPoints.get(clipPoints.size() - 1), 0);
                        else
                            render.drawLine(clipPoints.get(0), clipPoints.get(clipPoints.size() - 1), 0xFF0000, false, null);
                    }


            }

        private void pause()
            {
                render.clear();
                activeIrr = false;
                clipActive = false;

                drawDonePolygon();
            }
    }
