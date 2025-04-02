package headhunter_objects;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.util.ArrayList;

@Data
public class VacanciesList {
    @Expose
    ArrayList<Vacancy> items;
}