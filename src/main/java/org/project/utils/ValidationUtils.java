package org.project.utils;

import org.project.models.DNASequence;

import java.util.Collection;
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

    public static List<String> invertSequences(DNASequence DNASequence) {
        return IntStream.range(0, DNASequence.getDna().size())
                .mapToObj(value -> getInvertValues(DNASequence, value))
                .collect(Collectors.toList());
    }

    private static String getInvertValues(DNASequence DNASequence, int value) {
        return DNASequence.getDna().stream()
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
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
