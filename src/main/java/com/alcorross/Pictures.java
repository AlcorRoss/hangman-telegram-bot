package com.alcorross;

public enum Pictures {
    ERR_0("""
              |---|
              |
              |
              |
              ======"""),
    ERR_1("""
              |---|
              |   O
              |
              |
              ======"""),
    ERR_2("""
              |---|
              |   O
              |  /
              |
              ======"""),
    ERR_3("""
              |---|
              |   O
              |  /|
              |
              ======"""),
    ERR_4("""
              |---|
              |   O
              |  /|\\
              |
              ======"""),
    ERR_5("""
              |---|
              |   O
              |  /|\\
              |  /
              ======"""),
    ERR_6("""
              |---|
              |   O
              |  /|\\
              |   / \\
              ======""");
    private final String picture;

    Pictures(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return picture;
    }
}

