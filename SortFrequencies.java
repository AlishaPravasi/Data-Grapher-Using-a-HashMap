import java.util.Comparator;
import java.util.Map.Entry;

public class SortFrequencies implements Comparator<Entry<String, Double>>
{
	//compare elements to sort
	@Override
	public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
		// TODO Auto-generated method stub
		int newO1 = (int) Math.floor(o1.getValue());
		int newO2 = (int) Math.floor(o2.getValue());
		return newO2 - newO1;
	}
}
