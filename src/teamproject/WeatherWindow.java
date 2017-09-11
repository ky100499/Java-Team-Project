package teamproject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import java.util.Map;

public class WeatherWindow extends JFrame implements ActionListener {
    private JPanel container = new JPanel();
    private JPanel btnPanel = new JPanel();
    private JPanel imageContainer = new JPanel();

    private JLabel location = new JLabel();
    private JLabel date = new JLabel();
    private JLabel hour = new JLabel();
    private JLabel temperature = new JLabel();
    private JLabel skyExplain = new JLabel();
    private JLabel rainPercent = new JLabel();
    private JLabel rainAmount = new JLabel();
    private JLabel humidity = new JLabel();
    private JLabel windSpeed = new JLabel();
    private JLabel outsidePercent = new JLabel();
    private JLabel imageLabel;

    private JButton btnReload;
    private JButton btnExit;

    private ImageIcon weather;

    public WeatherWindow() {
        super("야외점호");
        this.setLayout(new BorderLayout());

        container.setLayout(new GridLayout(10, 1, 5, 5));
        container.setBorder(new EmptyBorder(10, 10, 10, 10));

        location.setFont(CustomFont.getDefaultFont());
        date.setFont(CustomFont.getDefaultFont());
        hour.setFont(CustomFont.getDefaultFont());
        temperature.setFont(CustomFont.getDefaultFont());
        skyExplain.setFont(CustomFont.getDefaultFont());
        rainPercent.setFont(CustomFont.getDefaultFont());
        rainAmount.setFont(CustomFont.getDefaultFont());
        humidity.setFont(CustomFont.getDefaultFont());
        windSpeed.setFont(CustomFont.getDefaultFont());
        outsidePercent.setFont(CustomFont.getDefaultFont());

        container.add(location);
        container.add(date);
        container.add(hour);
        container.add(temperature);
        container.add(skyExplain);
        container.add(rainPercent);
        container.add(rainAmount);
        container.add(humidity);
        container.add(windSpeed);
        container.add(outsidePercent);

        btnReload = new JButton("새로고침");
        btnExit = new JButton("닫기");

        btnReload.setFont(CustomFont.getDefaultFont());
        btnReload.setFont(CustomFont.getDefaultFont());

        btnReload.addActionListener(this);
        btnExit.addActionListener(this);

        btnPanel.add(btnReload);
        btnPanel.add(btnExit);

        imageLabel = new JLabel(this.weather);
        imageContainer.add(imageLabel);

        this.add(imageContainer, BorderLayout.NORTH);
        this.add(container);
        this.add(btnPanel, BorderLayout.SOUTH);

        this.getWeather();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void getWeather() {
        Map<String, String> weatherInfo = null;
        try {
            weatherInfo = WeatherInfo.getNextMorningWeather();
        } catch (Exception e) {
            String[] message = {e.getLocalizedMessage()};
            new Alert(this, message);
            System.exit(0);
        }

        String locationString;
        if (weatherInfo.get("location").equals("경기도 안산시단원구 와동")) {
            locationString = "경기도 안산시 단원구 와동";
        } else {
            String message[] = {"위치 정보에 문제가 발생했습니다.", "관리자 또는 개발자에게 문의해주세요"};
            new Alert(this, message);
            locationString = "N/A";
        }

        String[] dateString = weatherInfo.get("date").split("[^\\d]+");

        double probability;
        String holidayInfo = "";
        String response = null;
        try {
            response = Holiday.check(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2]));
            if (response.equalsIgnoreCase("fail")) {
                String[] message = {"Failed to load data"};
                new Alert(this, message);
                holidayInfo = " (Error)";
            }
        } catch (Exception e) {
            String[] message = {e.getLocalizedMessage()};
            new Alert(this, message);
            System.exit(0);
        }
        if (response.equalsIgnoreCase("none")) {
            probability = Probability.getProbability(Double.parseDouble(weatherInfo.get("temperature")), Double.parseDouble(weatherInfo.get("rainPercent")));
        } else {
            probability = 0;
            holidayInfo = " (" + response + ")";
        }

        location.setText("위치: " + locationString);
        date.setText("날짜: " + weatherInfo.get("date"));
        hour.setText("시각: " + weatherInfo.get("hour") + "시");
        temperature.setText("기온: " + weatherInfo.get("temperature") + " °C");
        skyExplain.setText("날씨: " + weatherInfo.get("korWeather"));
        rainPercent.setText("강수확률: " + weatherInfo.get("rainPercent") + " %");
        rainAmount.setText("예상강수량: " + weatherInfo.get("rainAmount") + " mm");
        humidity.setText("습도: " + weatherInfo.get("humidity") + " %");
        windSpeed.setText("풍속: " + weatherInfo.get("windSpeed") + " m/s");
        outsidePercent.setText("야외점호 확률: " + probability + " %" + holidayInfo);

        switch (Integer.parseInt(weatherInfo.get("rain"))) {
            case 0:
                switch (Integer.parseInt(weatherInfo.get("cloud"))) {
                    case 1:
                        weather = new ImageIcon("./images/weather/sunny.png");
                        break;
                    case 2:
                        weather = new ImageIcon("./images/weather/littleCloudy.png");
                        break;
                    case 3:
                    case 4:
                        weather = new ImageIcon("./images/weather/cloudy.png");
                        break;
                    default:
                        String message[] = {"기상 정보에 문제가 발생했습니다.", "관리자 또는 개발자에게 문의해주세요"};
                        new Alert(this, message);
                        weather = new ImageIcon("./images/error/error.png");
                }
                break;
            case 1:
            case 2:
                weather = new ImageIcon("./images/weather/rainy.png");
                break;
            case 3:
            case 4:
                weather = new ImageIcon("./images/weather/snow.png");
                break;
            default:
                String message[] = {"기상 정보에 문제가 발생했습니다.", "관리자 또는 개발자에게 문의해주세요"};
                new Alert(this, message);
                weather = new ImageIcon("./images/error/error.png");
        }
        imageLabel.setIcon(this.weather);

        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "닫기") {
            System.exit(0);
        } else if (e.getActionCommand() == "새로고침") {
            this.getWeather();
        }
    }
}