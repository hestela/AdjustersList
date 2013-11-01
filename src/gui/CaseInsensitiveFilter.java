package gui;

import javax.swing.RowFilter;

// Custom RowFilter made to ignore case when filtering r
public class CaseInsensitiveFilter extends RowFilter<Object, Object> {

    private final String match;

    public CaseInsensitiveFilter(String match) {
        this.match = match.toLowerCase();
    }

    @Override
    public boolean include(javax.swing.RowFilter.Entry<? extends Object, ? extends Object> entry) {
        for (int i = entry.getValueCount() - 1; i >= 0; i--) {
            if (entry.getStringValue(i).toLowerCase().contains(match)) {
              return true;
            }
          }
          return false;
    }            
};