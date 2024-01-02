import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Satellite extends JPanel {
    private int planetX = 300; // координаты центра планеты
    private int planetY = 300;
    private int planetRadius = 130; // радиус планеты
    private double satelliteAngle = 0; // угол, на котором находится спутник
    private int satelliteSemiMajorAxis = 200; // большая полуось эллипса
    private int satelliteSemiMinorAxis = 70; // малая полуось эллипса
    private double satelliteSpeed = 0.05; // скорость движения спутника
    private double time = 0; // время
    private boolean satelliteVisible = true;
    private boolean isInsidePlanet(int x, int y) {
        int distanceSquared = (x - planetX) * (x - planetX) + (y - planetY) * (y - planetY);
        int planetRadiusSquared = planetRadius * planetRadius;
        return distanceSquared <= planetRadiusSquared;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // отрисовываем планету
        g.setColor(Color.DARK_GRAY);
        g.fillOval(planetX - planetRadius, planetY - planetRadius, 2 * planetRadius, 2 * planetRadius);

        // Вычисляем координаты спутника только если он видим
        if (satelliteVisible) {
            int satelliteX = planetX + (int) (satelliteSemiMajorAxis * Math.cos(Math.toRadians(satelliteAngle)));
            int satelliteY = planetY + (int) (satelliteSemiMinorAxis * Math.sin(Math.toRadians(satelliteAngle)));
            // отрисовываем спутник
            g.setColor(Color.PINK);
            g.fillOval(satelliteX - 10, satelliteY - 10, 20, 20);
        }
    }

    public void simulateOrbit() {
        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                satelliteAngle = (double) (360 * Math.sin(satelliteSpeed * time)); // вычисляем угол в зависимости от времени
                time += 0.05; // увеличиваем время

                int satelliteX = planetX + (int) (satelliteSemiMajorAxis * Math.cos(Math.toRadians(satelliteAngle)));
                int satelliteY = planetY + (int) (satelliteSemiMinorAxis * Math.sin(Math.toRadians(satelliteAngle)));

                // Проверяем, находится ли спутник внутри планеты
                boolean insidePlanet = isInsidePlanet(satelliteX, satelliteY);

                // Проверяем, находится ли спутник на верхней дуге орбиты
                boolean onUpperOrbit = satelliteY <= planetY;

                // Спутник виден всегда, когда двигается по нижней дуге орбиты
                // И виден, пока не внутри планеты, когда двигается по верхней дуге орбиты
                satelliteVisible = !onUpperOrbit || (onUpperOrbit && !insidePlanet);

                repaint(); // перерисовываем окно
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Satellite Orbit Simulation");
        Satellite simulation = new Satellite();
        frame.add(simulation);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        simulation.simulateOrbit();
    }
}