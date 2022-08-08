package org.project.utils;

import org.project.models.ADNSequence;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class ValidationUtils
{

    private ValidationUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static int validatePatternMutant(List<String> adnSequence) {
        return adnSequence.stream()
                .filter(ValidationUtils::validateSequenceMutant)
                .collect(Collectors.counting()).intValue();
    }

    private static boolean validateSequenceMutant(String sequence)
    {
        Pattern pat = Pattern.compile("(.)\\1{3}");
        Matcher mat = pat.matcher(sequence);
        return mat.find();
    }

    public static List<String> invertSequences(ADNSequence adnSequence) {
        return IntStream.range(0, adnSequence.getDna().size())
                .mapToObj(value -> getInvertValues(adnSequence, value))
                .collect(Collectors.toList());
    }

    private static String getInvertValues(ADNSequence adnSequence, int value) {
        return adnSequence.getDna().stream()
                .map(sequence -> sequence.charAt(value))
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public static boolean areIntegerUpperOrEqualsThan(int a, int b)
    {
        return a >= b;
    }

    public static<T> List<T> concatenate(List<T>... lists)
    {
        return Stream.of(lists)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
    }
}
