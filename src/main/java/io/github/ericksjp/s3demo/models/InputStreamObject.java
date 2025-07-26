package io.github.ericksjp.s3demo.models;

import java.io.InputStream;

public record InputStreamObject(InputStream stream, Long size) {}
