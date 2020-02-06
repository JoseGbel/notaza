package my.apps;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import my.apps.skillstracker.R;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LocaleTest {

    private void setLocale(String language, String country) {
        Locale locale = new Locale(language, country);
        // here we update locale for date formatters
        Locale.setDefault(locale);
        // here we update locale for app resources
        Resources res = InstrumentationRegistry.getTargetContext().getResources();
        Configuration config = res.getConfiguration();
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    @Test
    public void testEnglishLocale() {
        setLocale("en", "EN");
        String editString = InstrumentationRegistry.getTargetContext().getString(R.string.edit);
        Assert.assertEquals("Edit", editString);
    }

    @Test
    public void testSpanishLocale() {
        setLocale("es", "ES");
        String editString = InstrumentationRegistry.getTargetContext().getString(R.string.edit);
        Assert.assertEquals("Editar", editString);
    }
}
