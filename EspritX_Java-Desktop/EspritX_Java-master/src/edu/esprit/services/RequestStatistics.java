package edu.esprit.services;

import edu.esprit.DAO.Request.RequestCRUD;
import edu.esprit.DAO.Service.ServiceCRUD;
import edu.esprit.entities.Group;
import edu.esprit.entities.Request;
import edu.esprit.entities.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RequestStatistics {

    ServiceCRUD SCRUD = new ServiceCRUD();
    List<Service> ListeService = SCRUD.ReadServices();

    RequestCRUD RCRUD = new RequestCRUD();
    List<Request> RequestsList = RCRUD.ReadRequests();

    private final SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public LongSummaryStatistics ResponseStatsByGroup(Group g) {
        Set<Integer> ServiceListIDs = ListeService.stream()
                .filter(service -> service.responsibleProperty().get().equals(g))
                .map(Service::getId)
                .collect(Collectors.toSet());
        Map<String, String> RequestListDates = RequestsList.stream()
                .filter(request -> (ServiceListIDs.contains(request.typeProperty().get().idProperty().get())) && (request.responded_atProperty().get() != null)) //replace responded_at condition with status
                .collect(Collectors.toMap(Request::getCreated_at, Request::getResponded_at));
        List<Long> ResponseTime = RequestListDates.entrySet().stream()
                .map(i -> {
                    long a = 0;
                    try {
                        long diff = dtf.parse(i.getValue()).getTime() - dtf.parse(i.getKey()).getTime();
                        a = TimeUnit.MILLISECONDS.toMinutes(diff);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return a;
                })
                .collect(Collectors.toList());
        LongSummaryStatistics stats = ResponseTime.stream()
                .mapToLong((x) -> x)
                .summaryStatistics();
        return stats;
    }

    public LongSummaryStatistics ResponseStatsByService(Service S) {
        Map<String, String> RequestListDates = RequestsList.stream()
                .filter(request -> (request.typeProperty().get().equals(S)) && (request.responded_atProperty().get() != null)) //replace responded_at condition with status
                .collect(Collectors.toMap(Request::getCreated_at, Request::getResponded_at));
        List<Long> ResponseTime = RequestListDates.entrySet().stream()
                .map(i -> {
                    long a = 0;
                    try {
                        long diff = dtf.parse(i.getValue()).getTime() - dtf.parse(i.getKey()).getTime();
                        a = TimeUnit.MILLISECONDS.toMinutes(diff);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return a;
                })
                .collect(Collectors.toList());
        LongSummaryStatistics stats = ResponseTime.stream()
                .mapToLong((x) -> x)
                .summaryStatistics();
        return stats;
    }
}
