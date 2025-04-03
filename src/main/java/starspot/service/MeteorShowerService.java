package starspot.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import starspot.model.MeteorShower;
import starspot.repository.MeteorShowerRepository;
import starspot.utils.DateUtil;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class MeteorShowerService {
    private final MeteorShowerRepository meteorShowerRepository;

    @Autowired
    public MeteorShowerService(MeteorShowerRepository meteorShowerRepository) {
        this.meteorShowerRepository = meteorShowerRepository;
    }

    public List<MeteorShower> getMeteorShowers() {
        List<MeteorShower> meteorShowers = meteorShowerRepository.getAllMeteorShowers().join();
        System.out.println("Fetched " + meteorShowers.size() + "meteor showers");
        return meteorShowers;
    }

    public void insertMeteorShowerData(MeteorShower meteorShower) {
        if (meteorShower != null) {
            meteorShowerRepository.save(meteorShower);
            System.out.println(" Meteor Shower saved: " + meteorShower.getEventId());
        }
    }

    //Turn meteor_showers.csv to firebase
    public void initMeteorShowerDataFromCSV() {
        try (CSVReader csvReader = new CSVReader(new FileReader("meteor_showers.csv"))) {
            String[] nextLine;
            csvReader.readNext();

            nextLine = csvReader.readNext();
            while (nextLine != null) {
                String headings = nextLine[0];          // heading
                String peakPeriod = nextLine[6];        //peak period
                List<ZonedDateTime> dateTimes = DateUtil.parseDateRangeAsUTC(peakPeriod);
                ZonedDateTime peakStartDate = dateTimes.get(0);
                MeteorShower meteorShower = new MeteorShower(headings, peakStartDate.toEpochSecond());
                meteorShower.setActivePeriod(nextLine[1]);
                meteorShower.setRadiant(nextLine[2]);
                meteorShower.setZhr(nextLine[3]);
                meteorShower.setVelocity(nextLine[4]);
                meteorShower.setParentObject(nextLine[5]);
                meteorShower.setPeakPeriod(nextLine[6]);
                meteorShower.setMoonFull(nextLine[7]);
                meteorShower.setDescription(nextLine[8]);

                this.insertMeteorShowerData(meteorShower);
                nextLine = csvReader.readNext();
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}
