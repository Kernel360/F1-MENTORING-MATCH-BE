package com.biengual.userapi.k6.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Controller
public class K6ApiController {

    @GetMapping("/k6")
    public String k6() {
        return "k6";
    }

    @PostMapping("/k6/run")
    public RedirectView runK6Script(@RequestParam String script, Model model) {
        String result = executeK6Script(script);
        model.addAttribute("result", result);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:3001/d/ee2c2qp50u6f4d/k6-load-testing-results?timezone=browser&var-Measurement=http_req_duration&var-Measurement=http_req_blocked&refresh=5s&from=now-1h&to=now"); // Grafana 대시보드 URL
        return redirectView;
    }

    private String executeK6Script(String script) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker-compose", "-f", "biengual/docker-compose-k6.yml", "up");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
        } catch (Exception e) {
            output.append("Error: ").append(e.getMessage());
        }
        return output.toString();
    }
}
