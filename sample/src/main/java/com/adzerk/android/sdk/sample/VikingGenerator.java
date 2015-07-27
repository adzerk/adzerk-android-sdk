package com.adzerk.android.sdk.sample;

import java.util.Random;

public class VikingGenerator {

    int positions;
    Random r;
    Quote[] quotes;

    public VikingGenerator(int positions) {
        this.positions = positions;
        quotes = new Quote[positions];
        r = new Random();
    }

    public static class Quote {
        public String url;
        public String name;
        public String quote;

        public Quote(String url, String name, String quote) {
            this.url = url;
            this.name = name;
            this.quote = quote;
        }
    }

    public int getCount() {
        return positions;
    }

    public Quote getQuote(int position) {
        if (position > positions) {
            throw new IllegalArgumentException("Position " + position + " exceeds maximum");
        }

        if (quotes[position] == null) {
            quotes[position] = new Quote(createHeadshotUrl(), createName(), createQuote());
        }

        return quotes[position];
    }

    private String createHeadshotUrl() {
        final int max = 88;
        return String.format("http://api.randomuser.me/portraits/med/women/%d.jpg", r.nextInt(max));
    }

    private String createName() {
        return NAMES[r.nextInt(NAMES.length)];
    }

    private String createQuote() {
        return QUOTES[r.nextInt(QUOTES.length)];
    }

    static final String[] NAMES = new String[] {
            "Hervor Ivar",
            "Marta Holta-Thorir",
            "Pernilla Sumarlida",
            "Hallkatla Soxol",
            "Hildigunnr Thrain",
            "Nina Gunnor",
            "Beate Hord",
            "Arnkatla Throst",
            "Anette Rennir",
            "Thorhildr Jorund",
            "Ingibjorg Eldiarn",
            "Dyrfinna Hroald",
            "Jarngeror Hastein",
            "Groa Armod",
            "Aegileif Orm",
            "Astra Askel",
            "Thurior Kolr",
            "Arnbjorg Thorbjorn",
            "Camilla Hedin",
            "Yrr Dag",
            "Eirny Ingimund",
            "Nina Sigmund",
            "Ketilrior Vandil",
            "Astrid Hafgrim",
            "Bera Herbjorn"
    };

    static final String[] QUOTES = new String[] {
            "Always rise to an early meal, but eat your fill before a feast.",
            "Never walk away from home ahead of your axe and sword.",
            "No lamb for the lazy wolf.",
            "Repay laughter with laughter but betrayal with treachery.",
            "Words of praise will never perish nor a noble name.",
            "One should not ask more than would be thought fitting.",
            "I demolish my bridges behind me - then there is no choice but forward",
            "The difficult is what takes a little time; the impossible is what takes a little longer.",
            "There seldom is a single wave.",
            "A bad rower blames the oar.",
            "Only dead fish follow the stream.",
            "A fair wind at our back is best.",
            "One must howl with the wolves one is among.",
            "If you cannot bite, never show your teeth.",
            "His hands are clean who warns another.",
            "Many go to the goat-house to get wool.",
            "One man's tale is but half a tale.",
            "Be not a braggart for if any work done be praise-worthy, others will sing your praises for you."
    };
}
