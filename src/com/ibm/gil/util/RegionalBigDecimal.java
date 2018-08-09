package com.ibm.gil.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;



/**
 * Insert the type's description here. Creation date: (8/6/2002 6:32:41 PM)
 * 
 * @author:
 */
public class RegionalBigDecimal extends Number implements Comparable {
    /**
     * The unscaled value of this RegionalBigDecimal, as returned by
     * unscaledValue().
     * 
     * @serial
     * @see #unscaledValue
     */
    private BigInteger intVal;

    /**
     * The scale of this RegionalBigDecimal, as returned by scale().
     * 
     * @serial
     * @see #scale
     */
    private static int defaultScale = 0;
    private int scale = 0;
    
    /**
     * The symbols that make up a decimal
     *  
     */
    private static DecimalFormatSymbols symbols = new DecimalFormat().getDecimalFormatSymbols();

    /* Appease the serialization gods */
    private static final long serialVersionUID = 6108874887143696463L;

    // Rounding Modes

    /**
     * Rounding mode to round away from zero. Always increments the digit prior
     * to a non-zero discarded fraction. Note that this rounding mode never
     * decreases the magnitude of the calculated value.
     */
    public final static int ROUND_UP = 0;

    /**
     * Rounding mode to round towards zero. Never increments the digit prior to
     * a discarded fraction (i.e., truncates). Note that this rounding mode
     * never increases the magnitude of the calculated value.
     */
    public final static int ROUND_DOWN = 1;

    /**
     * Rounding mode to round towards positive infinity. If the
     * RegionalBigDecimal is positive, behaves as for <tt>ROUND_UP</tt>; if
     * negative, behaves as for <tt>ROUND_DOWN</tt>. Note that this rounding
     * mode never decreases the calculated value.
     */
    public final static int ROUND_CEILING = 2;

    /**
     * Rounding mode to round towards negative infinity. If the
     * RegionalBigDecimal is positive, behave as for <tt>ROUND_DOWN</tt>; if
     * negative, behave as for <tt>ROUND_UP</tt>. Note that this rounding
     * mode never increases the calculated value.
     */
    public final static int ROUND_FLOOR = 3;

    /**
     * Rounding mode to round towards "nearest neighbor" unless both neighbors
     * are equidistant, in which case round up. Behaves as for <tt>ROUND_UP</tt>
     * if the discarded fraction is &gt;= .5; otherwise, behaves as for
     * <tt>ROUND_DOWN</tt>. Note that this is the rounding mode that most of
     * us were taught in grade school.
     */
    public final static int ROUND_HALF_UP = 4;

    /**
     * Rounding mode to round towards "nearest neighbor" unless both neighbors
     * are equidistant, in which case round down. Behaves as for
     * <tt>ROUND_UP</tt> if the discarded fraction is &gt; .5; otherwise,
     * behaves as for <tt>ROUND_DOWN</tt>.
     */
    public final static int ROUND_HALF_DOWN = 5;

    /**
     * Rounding mode to round towards the "nearest neighbor" unless both
     * neighbors are equidistant, in which case, round towards the even
     * neighbor. Behaves as for ROUND_HALF_UP if the digit to the left of the
     * discarded fraction is odd; behaves as for ROUND_HALF_DOWN if it's even.
     * Note that this is the rounding mode that minimizes cumulative error when
     * applied repeatedly over a sequence of calculations.
     */
    public final static int ROUND_HALF_EVEN = 6;

    /**
     * Rounding mode to assert that the requested operation has an exact result,
     * hence no rounding is necessary. If this rounding mode is specified on an
     * operation that yields an inexact result, an <tt>ArithmeticException</tt>
     * is thrown.
     */
    public final static int ROUND_UNNECESSARY = 7;

    public final static RegionalBigDecimal ZERO = new RegionalBigDecimal(0.00);

    public static transient char PERIODDECIMALSEPARATOR = '.';

    /**
     * Translates a double into a RegionalBigDecimal. The scale of the
     * RegionalBigDecimal is the smallest value such that
     * <tt>(10<sup>scale</sup> * val)</tt> is an integer.
     * <p>
     * Note: the results of this constructor can be somewhat unpredictable. One
     * might assume that <tt>new RegionalBigDecimal(.1)</tt> is exactly equal
     * to .1, but it is actually equal to
     * .1000000000000000055511151231257827021181583404541015625. This is so
     * because .1 cannot be represented exactly as a double (or, for that
     * matter, as a binary fraction of any finite length). Thus, the long value
     * that is being passed <i>in </i> to the constructor is not exactly equal
     * to .1, appearances nonwithstanding.
     * <p>
     * The (String) constructor, on the other hand, is perfectly predictable:
     * <tt>new RegionalBigDecimal(".1")</tt> is <i>exactly </i> equal to .1,
     * as one would expect. Therefore, it is generally recommended that the
     * (String) constructor be used in preference to this one.
     * 
     * @param val
     *            double value to be converted to RegionalBigDecimal.
     * @throws NumberFormatException
     *             <tt>val</tt> is equal to <tt>Double.NEGATIVE_INFINITY</tt>,
     *             <tt>Double.POSITIVE_INFINITY</tt>, or <tt>Double.NaN</tt>.
     */
    public RegionalBigDecimal(double val)
    {
        if (Double.isInfinite(val) || Double.isNaN(val))
            throw new NumberFormatException("Infinite or NaN");

        /*
         * Translate the double into sign, exponent and mantissa, according to
         * the formulae in JLS, Section 20.10.22.
         */
        long valBits = Double.doubleToLongBits(val);
        int sign = ((valBits >> 63) == 0 ? 1 : -1);
        int exponent = (int) ((valBits >> 52) & 0x7ffL);
        long mantissa = (exponent == 0 ? (valBits & ((1L << 52) - 1)) << 1 : (valBits & ((1L << 52) - 1)) | (1L << 52));
        exponent -= 1075;
        /* At this point, val == sign * mantissa * 2**exponent */

        /*
         * Special case zero to to supress nonterminating normalization and
         * bogus scale calculation.
         */
        if (mantissa == 0)
        {
            intVal = BigInteger.ZERO;
            return;
        }

        /* Normalize */
        while ((mantissa & 1) == 0)
        { /* i.e., Mantissa is even */
            mantissa >>= 1;
            exponent++;
        }

        /* Calculate intVal and scale */
        intVal = BigInteger.valueOf(sign * mantissa);
        if (exponent < 0)
        {
            intVal = intVal.multiply(BigInteger.valueOf(5).pow(-exponent));
            scale = -exponent;
        } else if (exponent > 0)
        {
            intVal = intVal.multiply(BigInteger.valueOf(2).pow(exponent));
        }
    }

    // Constructors

    /**
     * Translates the String representation of a BigDecmal into a
     * RegionalBigDecimal. The String representation consists of an optional
     * minus sign followed by a sequence of zero or more decimal digits,
     * optionally followed by a fraction. The fraction consists of of a decimal
     * point followed by zero or more decimal digits. The string must contain at
     * least one digit in the integer or fractional part. The scale of the
     * resulting RegionalBigDecimal will be the number of digits to the right of
     * the decimal point in the string, or 0 if the string contains no decimal
     * point. The character-to-digit mapping is provided by Character.digit. The
     * String may not contain any extraneous characters (whitespace, for
     * example).
     * 
     * @param val
     *            String representation of RegionalBigDecimal.
     * @throws NumberFormatException
     *             <tt>val</tt> is not a valid representation of a
     *             RegionalBigDecimal.
     * @see Character#digit
     */
    public RegionalBigDecimal(String val)
    {
        this(val, getDecimalFormatSymbols().getDecimalSeparator());
    }
    public RegionalBigDecimal(String val, char decimalSeparator)
    {
        
        if (val.length() == 0)
            val = "0";

        int pointPos = val.indexOf(decimalSeparator);
        if (pointPos == -1)
        { /* e.g. "123" */
            intVal = new BigInteger(val);
        } else if (pointPos == val.length() - 1)
        { /* e.g. "123." */
            intVal = new BigInteger(val.substring(0, val.length() - 1));
        } else
        { /* Fraction part exists */
            String fracString = val.substring(pointPos + 1);
            scale = fracString.length();
            BigInteger fraction = new BigInteger(fracString);
            if (val.charAt(pointPos + 1) == getDecimalFormatSymbols().getMinusSign()) /*
                                                                                       * ".-123"
                                                                                       * illegal!
                                                                                       */
                throw new NumberFormatException();
            if (pointPos == 0)
            { /* e.g. ".123" */
                intVal = fraction;
            } else if (val.charAt(0) == getDecimalFormatSymbols().getMinusSign() && pointPos == 1)
            {
                intVal = fraction.negate(); /* e.g. "-.123" */
            } else
            { /* e.g. "-123.456" */
                String intString = val.substring(0, pointPos);
                BigInteger intPart = new BigInteger(intString);
                if (val.charAt(0) == getDecimalFormatSymbols().getMinusSign())
                    fraction = fraction.negate();
                intVal = timesTenToThe(intPart, scale).add(fraction);
            }
        }
    }

    /**
     * Translates a BigInteger into a RegionalBigDecimal. The scale of the
     * RegionalBigDecimal is zero.
     * 
     * @param val
     *            BigInteger value to be converted to RegionalBigDecimal.
     */
    public RegionalBigDecimal(BigInteger val)
    {
        intVal = val;
    }

    /**
     * Translates a BigInteger unscaled value and an int scale into a
     * RegionalBigDecimal. The value of the RegionalBigDecimal is
     * <tt>(unscaledVal/10<sup>scale</sup>)</tt>.
     * 
     * @param unscaledVal
     *            unscaled value of the RegionalBigDecimal.
     * @param scale
     *            scale of the RegionalBigDecimal.
     * @throws NumberFormatException
     *             scale is negative
     */
    public RegionalBigDecimal(BigInteger unscaledVal, int scale)
    {
        if (scale < 0)
            throw new NumberFormatException("Negative scale");

        intVal = unscaledVal;
        this.scale = scale;
    }

    /**
     * Returns a RegionalBigDecimal whose value is the absolute value of this
     * RegionalBigDecimal, and whose scale is <tt>this.scale()</tt>.
     * 
     * @return <tt>abs(this)</tt>
     */
    public RegionalBigDecimal abs()
    {
        return (signum() < 0 ? negate() : this);
    }

    // Arithmetic Operations

    /**
     * Returns a RegionalBigDecimal whose value is <tt>(this + val)</tt>, and
     * whose scale is <tt>max(this.scale(), val.scale())</tt>.
     * 
     * @param val
     *            value to be added to this RegionalBigDecimal.
     * @return <tt>this + val</tt>
     */
    public RegionalBigDecimal add(RegionalBigDecimal val)
    {
        RegionalBigDecimal arg[] = new RegionalBigDecimal[2];
        arg[0] = this;
        arg[1] = val;
        matchScale(arg);
        return new RegionalBigDecimal(arg[0].intVal.add(arg[1].intVal), arg[0].scale);
    }

    // Comparison Operations

    /**
     * Compares this RegionalBigDecimal with the specified RegionalBigDecimal.
     * Two RegionalBigDecimals that are equal in value but have a different
     * scale (like 2.0 and 2.00) are considered equal by this method. This
     * method is provided in preference to individual methods for each of the
     * six boolean comparison operators (&lt;, ==, &gt;, &gt;=, !=, &lt;=). The
     * suggested idiom for performing these comparisons is:
     * <tt>(x.compareTo(y)</tt> &lt; <i>op </i>&gt; <tt>0)</tt>, where &lt;
     * <i>op </i>&gt; is one of the six comparison operators.
     * 
     * @param val
     *            RegionalBigDecimal to which this RegionalBigDecimal is to be
     *            compared.
     * @return -1, 0 or 1 as this RegionalBigDecimal is numerically less than,
     *         equal to, or greater than <tt>val</tt>.
     */
    public int compareTo(RegionalBigDecimal val)
    {
        /* Optimization: would run fine without the next three lines */
        int sigDiff = signum() - val.signum();
        if (sigDiff != 0)
            return (sigDiff > 0 ? 1 : -1);

        /* If signs match, scale and compare intVals */
        RegionalBigDecimal arg[] = new RegionalBigDecimal[2];
        arg[0] = this;
        arg[1] = val;
        matchScale(arg);
        return arg[0].intVal.compareTo(arg[1].intVal);
    }

    /**
     * Compares this RegionalBigDecimal with the specified Object. If the Object
     * is a RegionalBigDecimal, this method behaves like
     * <tt>compareTo(RegionalBigDecimal)</tt>. Otherwise, it throws a
     * <tt>ClassCastException</tt> (as RegionalBigDecimals are comparable only
     * to other RegionalBigDecimals).
     * 
     * @param o
     *            Object to which this RegionalBigDecimal is to be compared.
     * @return a negative number, zero, or a positive number as this
     *         RegionalBigDecimal is numerically less than, equal to, or greater
     *         than <tt>o</tt>, which must be a RegionalBigDecimal.
     * @throws ClassCastException
     *             <tt>o</tt> is not a RegionalBigDecimal.
     * @see #compareTo(java.math.BigDecimal)
     * @see Comparable
     * @since JDK1.2
     */
    public int compareTo(Object o)
    {
        return compareTo((RegionalBigDecimal) o);
    }

    /**
     * Returns a RegionalBigDecimal whose value is <tt>(this / val)</tt>, and
     * whose scale is <tt>this.scale()</tt>. If rounding must be performed to
     * generate a result with the given scale, the specified rounding mode is
     * applied.
     * 
     * @param val
     *            value by which this RegionalBigDecimal is to be divided.
     * @param roundingMode
     *            rounding mode to apply.
     * @return <tt>this / val</tt>
     * @throws ArithmeticException
     *             <tt>val==0</tt>, or
     *             <tt>roundingMode==ROUND_UNNECESSARY</tt> and
     *             <tt>this.scale()</tt> is insufficient to represent the
     *             result of the division exactly.
     * @throws IllegalArgumentException
     *             <tt>roundingMode</tt> does not represent a valid rounding
     *             mode.
     * @see #ROUND_UP
     * @see #ROUND_DOWN
     * @see #ROUND_CEILING
     * @see #ROUND_FLOOR
     * @see #ROUND_HALF_UP
     * @see #ROUND_HALF_DOWN
     * @see #ROUND_HALF_EVEN
     * @see #ROUND_UNNECESSARY
     */
    public RegionalBigDecimal divide(RegionalBigDecimal val, int roundingMode)
    {
        return this.divide(val, scale, roundingMode);
    }

    /**
     * Returns a RegionalBigDecimal whose value is <tt>(this / val)</tt>, and
     * whose scale is as specified. If rounding must be performed to generate a
     * result with the specified scale, the specified rounding mode is applied.
     * 
     * @param val
     *            value by which this RegionalBigDecimal is to be divided.
     * @param scale
     *            scale of the RegionalBigDecimal quotient to be returned.
     * @param roundingMode
     *            rounding mode to apply.
     * @return <tt>this / val</tt>
     * @throws ArithmeticException
     *             <tt>val</tt> is zero, <tt>scale</tt> is negative, or
     *             <tt>roundingMode==ROUND_UNNECESSARY</tt> and the specified
     *             scale is insufficient to represent the result of the division
     *             exactly.
     * @throws IllegalArgumentException
     *             <tt>roundingMode</tt> does not represent a valid rounding
     *             mode.
     * @see #ROUND_UP
     * @see #ROUND_DOWN
     * @see #ROUND_CEILING
     * @see #ROUND_FLOOR
     * @see #ROUND_HALF_UP
     * @see #ROUND_HALF_DOWN
     * @see #ROUND_HALF_EVEN
     * @see #ROUND_UNNECESSARY
     */
    public RegionalBigDecimal divide(RegionalBigDecimal val, int scale, int roundingMode)
    {
        if (scale < 0)
            throw new ArithmeticException("Negative scale");
        if (roundingMode < ROUND_UP || roundingMode > ROUND_UNNECESSARY)
            throw new IllegalArgumentException("Invalid rounding mode");

        /*
         * Rescale dividend or divisor (whichever can be "upscaled" to produce
         * correctly scaled quotient).
         */
        RegionalBigDecimal dividend, divisor;
        if (scale + val.scale >= this.scale)
        {
            dividend = this.setScale(scale + val.scale);
            divisor = val;
        } else
        {
            dividend = this;
            divisor = val.setScale(this.scale - scale);
        }

        /* Do the division and return result if it's exact */
        BigInteger i[] = dividend.intVal.divideAndRemainder(divisor.intVal);
        BigInteger q = i[0], r = i[1];
        if (r.signum() == 0)
            return new RegionalBigDecimal(q, scale);
        else if (roundingMode == ROUND_UNNECESSARY) /* Rounding prohibited */
            throw new ArithmeticException("Rounding necessary");

        /* Round as appropriate */
        int signum = dividend.signum() * divisor.signum(); /* Sign of result */
        boolean increment;
        if (roundingMode == ROUND_UP)
        { /* Away from zero */
            increment = true;
        } else if (roundingMode == ROUND_DOWN)
        { /* Towards zero */
            increment = false;
        } else if (roundingMode == ROUND_CEILING)
        { /* Towards +infinity */
            increment = (signum > 0);
        } else if (roundingMode == ROUND_FLOOR)
        { /* Towards -infinity */
            increment = (signum < 0);
        } else
        { /* Remaining modes based on nearest-neighbor determination */
            int cmpFracHalf = r.abs().multiply(BigInteger.valueOf(2)).compareTo(divisor.intVal.abs());
            if (cmpFracHalf < 0)
            { /* We're closer to higher digit */
                increment = false;
            } else if (cmpFracHalf > 0)
            { /* We're closer to lower digit */
                increment = true;
            } else
            { /* We're dead-center */
                if (roundingMode == ROUND_HALF_UP)
                    increment = true;
                else if (roundingMode == ROUND_HALF_DOWN)
                    increment = false;
                else
                    /* roundingMode == ROUND_HALF_EVEN */
                    increment = q.testBit(0); /* true iff q is odd */
            }
        }
        return (increment ? new RegionalBigDecimal(q.add(BigInteger.valueOf(signum)), scale) : new RegionalBigDecimal(q, scale));
    }

    /**
     * Converts this RegionalBigDecimal to a double. Similar to the
     * double-to-float <i>narrowing primitive conversion </i> defined in <i>The
     * Java Language Specification </i>: if this RegionalBigDecimal has too
     * great a magnitude to represent as a double, it will be converted to
     * <tt>DOUBLE.NEGATIVE_INFINITY</tt> or <tt>DOUBLE.POSITIVE_INFINITY</tt>
     * as appropriate.
     * 
     * @return this RegionalBigDecimal converted to a double.
     */
    public double doubleValue()
    {
        /* Somewhat inefficient, but guaranteed to work. */
        return Double.valueOf(this.toString()).doubleValue();
    }

    /**
     * Converts this RegionalBigDecimal to a BigDecimal.
     * 
     * @return this RegionalBigDecimal converted to a BigDecimal.
     */
    public BigDecimal bigDecimalValue()
    {
        /* Somewhat inefficient, but guaranteed to work. */
        return new BigDecimal(this.toString());
    }

    /**
     * Compares this RegionalBigDecimal with the specified Object for equality.
     * Unlike compareTo, this method considers two RegionalBigDecimals equal
     * only if they are equal in value and scale (thus 2.0 is not equal to 2.00
     * when compared by this method).
     * 
     * @param o
     *            Object to which this RegionalBigDecimal is to be compared.
     * @return <tt>true</tt> if and only if the specified Object is a
     *         RegionalBigDecimal whose value and scale are equal to this
     *         RegionalBigDecimal's.
     * @see #compareTo(java.math.BigDecimal)
     */
    public boolean equals(Object x)
    {
        if (!(x instanceof RegionalBigDecimal))
            return false;
        RegionalBigDecimal xDec = (RegionalBigDecimal) x;

        return scale == xDec.scale && intVal.equals(xDec.intVal);
    }

    /**
     * Converts this RegionalBigDecimal to a float. Similar to the
     * double-to-float <i>narrowing primitive conversion </i> defined in <i>The
     * Java Language Specification </i>: if this RegionalBigDecimal has too
     * great a magnitude to represent as a float, it will be converted to
     * <tt>FLOAT.NEGATIVE_INFINITY</tt> or <tt>FLOAT.POSITIVE_INFINITY</tt>
     * as appropriate.
     * 
     * @return this RegionalBigDecimal converted to a float.
     */
    public float floatValue()
    {
        /* Somewhat inefficient, but guaranteed to work. */
        return Float.valueOf(this.toString()).floatValue();
    }

    public static DecimalFormatSymbols getDecimalFormatSymbols()
    {
        return symbols;
    }

    // Hash Function

    /**
     * Returns the hash code for this RegionalBigDecimal. Note that two
     * RegionalBigDecimals that are numerically equal but differ in scale (like
     * 2.0 and 2.00) will generally <i>not </i> have the same hash code.
     * 
     * @return hash code for this RegionalBigDecimal.
     */
    public int hashCode()
    {
        return 31 * intVal.hashCode() + scale;
    }

    /**
     * Converts this RegionalBigDecimal to an int. Standard <i>narrowing
     * primitive conversion </i> as defined in <i>The Java Language
     * Specification </i>: any fractional part of this RegionalBigDecimal will
     * be discarded, and if the resulting "BigInteger" is too big to fit in an
     * int, only the low-order 32 bits are returned.
     * 
     * @return this RegionalBigDecimal converted to an int.
     */
    public int intValue()
    {
        return toBigInteger().intValue();
    }

    /**
     * Converts this RegionalBigDecimal to a long. Standard <i>narrowing
     * primitive conversion </i> as defined in <i>The Java Language
     * Specification </i>: any fractional part of this RegionalBigDecimal will
     * be discarded, and if the resulting "BigInteger" is too big to fit in a
     * long, only the low-order 64 bits are returned.
     * 
     * @return this RegionalBigDecimal converted to an int.
     */
    public long longValue()
    {
        return toBigInteger().longValue();
    }

    /*
     * If the scales of val[0] and val[1] differ, rescale (non-destructively)
     * the lower-scaled RegionalBigDecimal so they match.
     */
    private static void matchScale(RegionalBigDecimal[] val)
    {
        if (val[0].scale < val[1].scale)
            val[0] = val[0].setScale(val[1].scale);
        else if (val[1].scale < val[0].scale)
            val[1] = val[1].setScale(val[0].scale);
    }

    /**
     * Returns the maximum of this RegionalBigDecimal and <tt>val</tt>.
     * 
     * @param val
     *            value with with the maximum is to be computed.
     * @return the RegionalBigDecimal whose value is the greater of this
     *         RegionalBigDecimal and <tt>val</tt>. If they are equal, as
     *         defined by the <tt>compareTo</tt> method, either may be
     *         returned.
     * @see #compareTo(java.math.BigDecimal)
     */
    public RegionalBigDecimal max(RegionalBigDecimal val)
    {
        return (compareTo(val) > 0 ? this : val);
    }

    /**
     * Returns the minimum of this RegionalBigDecimal and <tt>val</tt>.
     * 
     * @param val
     *            value with with the minimum is to be computed.
     * @return the RegionalBigDecimal whose value is the lesser of this
     *         RegionalBigDecimal and <tt>val</tt>. If they are equal, as
     *         defined by the <tt>compareTo</tt> method, either may be
     *         returned.
     * @see #compareTo(java.math.BigDecimal)
     */
    public RegionalBigDecimal min(RegionalBigDecimal val)
    {
        return (compareTo(val) < 0 ? this : val);
    }

    // Decimal Point Motion Operations

    /**
     * Returns a RegionalBigDecimal which is equivalent to this one with the
     * decimal point moved n places to the left. If n is non-negative, the call
     * merely adds n to the scale. If n is negative, the call is equivalent to
     * movePointRight(-n). (The RegionalBigDecimal returned by this call has
     * value <tt>(this * 10<sup>-n</sup>)</tt> and scale
     * <tt>max(this.scale()+n, 0)</tt>.)
     * 
     * @param n
     *            number of places to move the decimal point to the left.
     * @return a RegionalBigDecimal which is equivalent to this one with the
     *         decimal point moved <tt>n</tt> places to the left.
     */
    public RegionalBigDecimal movePointLeft(int n)
    {
        return (n >= 0 ? new RegionalBigDecimal(intVal, scale + n) : movePointRight(-n));
    }

    /**
     * 
     * Moves the decimal point the specified number of places to the right. If
     * this RegionalBigDecimal's scale is &gt;= <tt>n</tt>, the call merely
     * subtracts <tt>n</tt> from the scale; otherwise, it sets the scale to
     * zero, and multiplies the integer value by
     * <tt>10<sup>(n - this.scale)</sup></tt>. If <tt>n</tt> is negative,
     * the call is equivalent to <tt>movePointLeft(-n)</tt>. (The
     * RegionalBigDecimal returned by this call has value
     * <tt>(this * 10<sup>n</sup>)</tt> and scale
     * <tt>max(this.scale()-n, 0)</tt>.)
     * 
     * @param n
     *            number of places to move the decimal point to the right.
     * @return a RegionalBigDecimal which is equivalent to this one with the
     *         decimal point moved <tt>n</tt> places to the right.
     */
    public RegionalBigDecimal movePointRight(int n)
    {
        return (scale >= n ? new RegionalBigDecimal(intVal, scale - n) : new RegionalBigDecimal(timesTenToThe(intVal, n - scale), 0));
    }

    /**
     * Returns a RegionalBigDecimal whose value is <tt>(this * val)</tt>, and
     * whose scale is <tt>(this.scale() + val.scale())</tt>.
     * 
     * @param val
     *            value to be multiplied by this RegionalBigDecimal.
     * @return <tt>this * val</tt>
     */
    public RegionalBigDecimal multiply(RegionalBigDecimal val)
    {
        return new RegionalBigDecimal(intVal.multiply(val.intVal), scale + val.scale);
    }

    /**
     * Returns a RegionalBigDecimal whose value is <tt>(-this)</tt>, and
     * whose scale is this.scale().
     * 
     * @return <tt>-this</tt>
     */
    public RegionalBigDecimal negate()
    {
        return new RegionalBigDecimal(intVal.negate(), scale);
    }

    /**
     * Reconstitute the <tt>BigDecimal</tt> instance from a stream (that is,
     * deserialize it).
     */
    private synchronized void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException
    {
        // Read in all fields
        s.defaultReadObject();

        // Validate scale factor
        if (scale < 0)
            throw new java.io.StreamCorruptedException("BigDecimal: Negative scale");
    }

    /**
     * Returns the <i>scale </i> of this RegionalBigDecimal. (The scale is the
     * number of digits to the right of the decimal point.)
     * 
     * @return the scale of this RegionalBigDecimal.
     */
    public int scale()
    {
        return scale;
    }

    public static void setDefaultScale(int value)
    {
        defaultScale = value;
    }

    public static int getDefaultScale()
    {
        return defaultScale;
    }

    public static void setDecimalFormatSymbols(DecimalFormatSymbols newSymbols)
    {
        symbols = newSymbols;
    }

    /**
     * Returns a RegionalBigDecimal whose scale is the specified value, and
     * whose value is numerically equal to this RegionalBigDecimal's. Throws an
     * ArithmeticException if this is not possible. This call is typically used
     * to increase the scale, in which case it is guaranteed that there exists a
     * RegionalBigDecimal of the specified scale and the correct value. The call
     * can also be used to reduce the scale if the caller knows that the
     * RegionalBigDecimal has sufficiently many zeros at the end of its
     * fractional part (i.e., factors of ten in its integer value) to allow for
     * the rescaling without loss of precision.
     * <p>
     * Note that this call returns the same result as the two argument version
     * of setScale, but saves the caller the trouble of specifying a rounding
     * mode in cases where it is irrelevant.
     * 
     * @param scale
     *            scale of the RegionalBigDecimal value to be returned.
     * @return a RegionalBigDecimal whose scale is the specified value, and
     *         whose unscaled value is determined by multiplying or dividing
     *         this RegionalBigDecimal's unscaled value by the appropriate power
     *         of ten to maintain its overall value.
     * @throws ArithmeticException
     *             <tt>scale</tt> is negative, or the specified scaling
     *             operation would require rounding.
     * @see #setScale(int, int)
     */
    public RegionalBigDecimal setScale(int scale)
    {
        return setScale(scale, ROUND_HALF_UP);
    }

    // Scaling/Rounding Operations

    /**
     * Returns a RegionalBigDecimal whose scale is the specified value, and
     * whose unscaled value is determined by multiplying or dividing this
     * RegionalBigDecimal's unscaled value by the appropriate power of ten to
     * maintain its overall value. If the scale is reduced by the operation, the
     * unscaled value must be divided (rather than multiplied), and the value
     * may be changed; in this case, the specified rounding mode is applied to
     * the division.
     * 
     * @param scale
     *            scale of the RegionalBigDecimal value to be returned.
     * @return a RegionalBigDecimal whose scale is the specified value, and
     *         whose unscaled value is determined by multiplying or dividing
     *         this RegionalBigDecimal's unscaled value by the appropriate power
     *         of ten to maintain its overall value.
     * @throws ArithmeticException
     *             <tt>scale</tt> is negative, or
     *             <tt>roundingMode==ROUND_UNNECESSARY</tt> and the specified
     *             scaling operation would require rounding.
     * @throws IllegalArgumentException
     *             <tt>roundingMode</tt> does not represent a valid rounding
     *             mode.
     * @see #ROUND_UP
     * @see #ROUND_DOWN
     * @see #ROUND_CEILING
     * @see #ROUND_FLOOR
     * @see #ROUND_HALF_UP
     * @see #ROUND_HALF_DOWN
     * @see #ROUND_HALF_EVEN
     * @see #ROUND_UNNECESSARY
     */
    public RegionalBigDecimal setScale(int scale, int roundingMode)
    {
        if (scale < 0)
            throw new ArithmeticException("Negative scale");
        if (roundingMode < ROUND_UP || roundingMode > ROUND_UNNECESSARY)
            throw new IllegalArgumentException("Invalid rounding mode");

        /* Handle the easy cases */
        if (scale == this.scale)
            return this;
        else if (scale > this.scale)
            return new RegionalBigDecimal(timesTenToThe(intVal, scale - this.scale), scale);
        else
            /* scale < this.scale */
            return divide(valueOf(1), scale, roundingMode);
    }

    /**
     * Returns the signum function of this RegionalBigDecimal.
     * 
     * @return -1, 0 or 1 as the value of this RegionalBigDecimal is negative,
     *         zero or positive.
     */
    public int signum()
    {
        return intVal.signum();
    }

    /**
     * Returns a RegionalBigDecimal whose value is <tt>(this - val)</tt>, and
     * whose scale is <tt>max(this.scale(), val.scale())</tt>.
     * 
     * @param val
     *            value to be subtracted from this RegionalBigDecimal.
     * @return <tt>this - val</tt>
     */
    public RegionalBigDecimal subtract(RegionalBigDecimal val)
    {
        RegionalBigDecimal arg[] = new RegionalBigDecimal[2];
        arg[0] = this;
        arg[1] = val;
        matchScale(arg);
        return new RegionalBigDecimal(arg[0].intVal.subtract(arg[1].intVal), arg[0].scale);
    }

    // Private "Helper" Methods

    /* Returns (a * 10^b) */
    private static BigInteger timesTenToThe(BigInteger a, int b)
    {
        return a.multiply(BigInteger.valueOf(10).pow(b));
    }

    /**
     * Converts this RegionalBigDecimal to a BigInteger. Standard <i>narrowing
     * primitive conversion </i> as defined in <i>The Java Language
     * Specification </i>: any fractional part of this RegionalBigDecimal will
     * be discarded.
     * 
     * @return this RegionalBigDecimal converted to a BigInteger.
     */
    public BigInteger toBigInteger()
    {
        return (scale == 0 ? intVal : intVal.divide(BigInteger.valueOf(10).pow(scale)));
    }
    
    public BigDecimal toBigDecimal()
    {
        return new BigDecimal(toString(RegionalBigDecimal.PERIODDECIMALSEPARATOR));
    }

    public Double toDouble()
    {
        return new Double(toString(RegionalBigDecimal.PERIODDECIMALSEPARATOR));
    }

    // Format Converters

    /**
     * Returns the string representation of this RegionalBigDecimal. The
     * digit-to- character mapping provided by <tt>Character.forDigit</tt> is
     * used. A leading minus sign is used to indicate sign, and the number of
     * digits to the right of the decimal point is used to indicate scale. (This
     * representation is compatible with the (String) constructor.)
     * 
     * @return String representation of this RegionalBigDecimal.
     * @see Character#forDigit
     * @see #BigDecimal(java.lang.String)
     */
    public String toString()
    {
        return toString (getDecimalFormatSymbols().getDecimalSeparator());
    }
    
    public String toString(char decimalSeparator)
    {
        if (scale == 0) /* No decimal point */
            return intVal.toString();

        /* Insert decimal point */
        StringBuffer buf;
        String intString = intVal.abs().toString();
        int signum = signum();
        int insertionPoint = intString.length() - scale;
        if (insertionPoint == 0)
        { /* Point goes right before intVal */
            return (signum < 0 ? symbols.getMinusSign() + "0" + decimalSeparator : "0" + decimalSeparator) + intString;
        } else if (insertionPoint > 0)
        { /* Point goes inside intVal */
            buf = new StringBuffer(intString);
            buf.insert(insertionPoint, decimalSeparator);
            if (signum < 0)
                buf.insert(0, symbols.getMinusSign());
        } else
        { /* We must insert zeros between point and intVal */
            buf = new StringBuffer(3 - insertionPoint + intString.length());
            buf.append(signum < 0 ? symbols.getMinusSign() + "0" + decimalSeparator : "0" + decimalSeparator);
            for (int i = 0; i < -insertionPoint; i++)
                buf.append('0');
            buf.append(intString);
        }
        return buf.toString();
    }

    /**
     * Returns a BigInteger whose value is the <i>unscaled value </i> of this
     * RegionalBigDecimal. (Computes <tt>(this * 10<sup>this.scale()</sup>)</tt>.)
     * 
     * @return the unscaled value of this RegionalBigDecimal.
     * @since JDK1.2
     */
    public BigInteger unscaledValue()
    {
        return intVal;
    }

    /**
     * Translates a long value into a RegionalBigDecimal with a scale of zero.
     * This "static factory method" is provided in preference to a (long)
     * constructor because it allows for reuse of frequently used
     * RegionalBigDecimals.
     * 
     * @param val
     *            value of the RegionalBigDecimal.
     * @return a RegionalBigDecimal whose value is <tt>val</tt>.
     */
    public static RegionalBigDecimal valueOf(long val)
    {
        return valueOf(val, 0);
    }

    // Static Factory Methods

    /**
     * Translates a long unscaled value and an int scale into a
     * RegionalBigDecimal. This "static factory method" is provided in
     * preference to a (long, int) constructor because it allows for reuse of
     * frequently used RegionalBigDecimals.
     * 
     * @param unscaledVal
     *            unscaled value of the RegionalBigDecimal.
     * @param scale
     *            scale of the RegionalBigDecimal.
     * @return a RegionalBigDecimal whose value is
     *         <tt>(unscaledVal/10<sup>scale</sup>)</tt>.
     */
    public static RegionalBigDecimal valueOf(long unscaledVal, int scale)
    {
        return new RegionalBigDecimal(BigInteger.valueOf(unscaledVal), scale);
    }
}