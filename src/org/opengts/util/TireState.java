// ----------------------------------------------------------------------------
// Copyright 2007-2017, GeoTelematic Solutions, Inc.
// All rights reserved
// ----------------------------------------------------------------------------
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// ----------------------------------------------------------------------------
// Description:
//  Base64 encoding/decoding
// ----------------------------------------------------------------------------
// Change History:
//  2017/02/02  Martin D. Flynn
//     -Initial release
// ----------------------------------------------------------------------------
package org.opengts.util;

import java.util.*;
import java.io.*;

/**
*** Tire state 
**/

public class TireState
{

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    private static final int    DFT_MAX_TIRES       = 64; // absolute max number of tires
    private static final int    DFT_MAX_AXLES       = 16;
    private static final int    DFT_TIRES_PER_AXLE  =  4;

    private static       int    MAX_TIRES           = DFT_MAX_TIRES;
    private static       int    MAX_AXLES           = DFT_MAX_AXLES;
    private static       int    TIRES_PER_AXLE      = DFT_TIRES_PER_AXLE;

    /**
    *** Sets the configures default number of tires per axle
    *** @param tpa The tires-per-axle value (valid values 2,4,6,8)
    **/
    public static void SetTiresPerAxle(int tpa)
    {

        /* constrain tires-per-axle */
        if (tpa <= 0) {
            tpa = DFT_TIRES_PER_AXLE;
        } else
        if (tpa > 6) {
            tpa = DFT_TIRES_PER_AXLE;
        }

        /* round up to next even number */
        if ((tpa & 1) != 0) {
            tpa++;
        }

        /* constrain max tires and max axles */
        TIRES_PER_AXLE = tpa; // 2=32, 4=16, 6=10*, 8=8
        MAX_AXLES      = DFT_MAX_TIRES / tpa; // truncate
        MAX_TIRES      = MAX_AXLES * tpa;

    }

    /**
    *** Gets the configured default number of tires per axle
    *** @return The configured number of tires per axle
    **/
    public static int GetTiresPerAxle()
    {
        return TIRES_PER_AXLE;
    }

    /**
    *** Gets the configured default maximum number of tires
    *** @return The maximum number of supported tires
    **/
    public static int GetMaximumTires()
    {
        return MAX_TIRES;
    }

    /**
    *** Gets the configured default maximum number of axles
    *** @return The maximum number of supported axles
    **/
    public static int GetMaximumAxles()
    {
        return MAX_AXLES;
    }

    /**
    *** Gets the tire index for the specified axle/tire index
    *** @param axleNdx      The axle index
    *** @param axleTireNdx  The index of the tire on the axle (fron left to right)
    *** @return The tire index corresponding to the specified axle/tire index.
    **/
    public static int GetTireIndex(int axleNdx, int axleTireNdx)
    {
        return (axleNdx >= 0)? ((axleNdx * GetTiresPerAxle()) + axleTireNdx) : axleTireNdx;
    }

    /**
    *** Gets the axle index for the specified tire index
    *** @param tireNdx  The tire index
    *** @return The axle index corresponding to the specified tire index
    **/
    public static int GetAxleIndex(int tireNdx)
    {
        return (tireNdx >= 0)? (tireNdx / GetTiresPerAxle()) : -1;
    }

    /**
    *** Gets the axle/tire index for the specified tire index
    *** @param tireNdx  The tire index
    *** @return The tire index on the axle corresponding to the specified tire index
    **/
    public static int GetAxleTireIndex(int tireNdx)
    {
        return (tireNdx >= 0)? (tireNdx % GetTiresPerAxle()) : -1;
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    public static final double  PSI_PER_KPA         = 0.14503773773020923;      
    public static final double  KPA_PER_PSI         = 1.0 / PSI_PER_KPA; // 6.89475729316836
    public static final double  KPA_PER_BAR         = 100.0;
    public static final double  BAR_PER_KPA         = 1.0 / KPA_PER_BAR;      
    public static final double  BAR_PER_PSI         = BAR_PER_KPA * KPA_PER_PSI; // 0.06894757293168
    public static final double  PSI_PER_BAR         = PSI_PER_KPA * KPA_PER_BAR; // 14.50377377302092
    public static final double  INVALID_PRESSURE    = -999.0;

    /**
    *** Returns true is the specified pressure is valid, false otherwise.
    *** (this method assumes that a zero or negative pressure is invalid)
    **/
    public static boolean IsValidPressure(double P)
    {
        return (!Double.isNaN(P) && (P > 0.0) && (P <= 999.0))? true : false;
    }

    /**
    *** kPa to PSI
    *** @param kPa kPa pressure
    *** @return PSI pressure
    **/
    public static double kPa2PSI(double kPa)
    {
        return kPa * PSI_PER_KPA;
    }

    /**
    *** PSI to kPa
    *** @param psi PSI pressure
    *** @return kPa pressure
    **/
    public static double PSI2kPa(double psi)
    {
        return psi * KPA_PER_PSI;
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    public static final double  TEMP_LIMIT_LO       =  -273.15; // degrees C (absolute Kelvin)
    public static final double  TEMP_LIMIT_HI       =   500.0;  // degrees C
    public static final double  INVALID_TEMPERATURE = -9999.0;  // degrees C

    /**
    *** Returns true is the specified temperature is valid, false otherwise.
    *** @param C  The temperature to test
    **/
    public static boolean IsValidTemperature(double C)
    {
        return (!Double.isNaN(C) && (C >= TEMP_LIMIT_LO) && (C <= TEMP_LIMIT_HI))? true : false;
    }

    /**
    *** Fahrenheit to Celsius
    *** @param F Fahrenheit temperature
    *** @return Celsius temperature
    **/
    public static double F2C(double F)
    {
        return (F - 32.0) * 5.0 / 9.0;
    }

    /**
    *** Celsius to Fahrenheit
    *** @param C Celsius temperature
    *** @return Fahrenheit temperature
    **/
    public static double C2F(double C)
    {
        return (C * 9.0 / 5.0) + 32.0;
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
    *** Updates the specified TireState with values from the specified String.
    *** If the specified TireState is null, it will be created and returned if the String 
    *** contains valid parsable data.  May return null if the specified TireState is null 
    *** and the String cannot be parsed. 
    *** If the TireState is created within this method, it will not have had the tire
    *** index set.
    *** @param ts        The TireState that will be updated
    *** @param s         The String containing the tire state information to add to the TireState instance
    *** @param tempOnly  True if the String contains only temperature values
    **/
    public static TireState UpdateTireState(TireState ts, String s, boolean tempOnly)
    {
        if (!StringTools.isBlank(s)) {
            // -- Pressure   :  "ActualPress[/PreferredPress][,Temperature]"
            // -- Temperature:  "Temperature"
            String PT[] = StringTools.split(s,',');
            String Pv   = (PT.length > 0)? PT[0] : null;
            String Tv   = (PT.length > 1)? PT[1] : null;

            // -- "Temperature" only?
            if (tempOnly) {
                if (StringTools.isBlank(Tv)) {
                    // -- this is the expected condition
                    Tv = Pv;    // move pressure to temperature
                    Pv = null;  // clear pressure
                } else {
                    // -- "tempOnly" is lying! (leaving "Tv" as is)
                }
            }

            // -- "Actual[/Preferred]" pressure
            if (!StringTools.isBlank(Pv)) {
                String _P[] = StringTools.split(Pv,'/');
                if (_P.length > 0) {
                    double P = StringTools.parseDouble(_P[0],INVALID_PRESSURE);
                    if (IsValidPressure(P)) {
                        if (ts == null) { ts = new TireState(); }
                        ts.setActualPressure(P);
                    }
                }
                if (_P.length > 1) {
                    double P = StringTools.parseDouble(_P[1],INVALID_PRESSURE);
                    if (IsValidPressure(P)) {
                        if (ts == null) { ts = new TireState(); }
                        ts.setPreferredPressure(P);
                    }
                }
            }

            // -- "Temperature"
            if (!StringTools.isBlank(Tv)) {
                double T = StringTools.parseDouble(Tv,INVALID_TEMPERATURE);
                if (IsValidTemperature(T)) {
                    if (ts == null) { ts = new TireState(); }
                    ts.setActualTemperature(T);
                }
            }

        } 
        return ts;
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
    *** Parses the tire index from the specified String
    *** @param t  The String containing the tire index information in the format
    ***             "Txx", "Taa-x", or "Taax".
    *** @return The parsed tire index
    **/
    public static int ParseTireIndex(String t)
    {
        // "Txx", "Taa-x"
        if (StringTools.isBlank(t)) {
            return -1;
        }
        String Tx = t.startsWith("T")? t.substring(1).trim() : t;
        // -- 
        if ((Tx.length() == 1) || (Tx.length() == 2)) {
            // -- "[T]xx"
            int tireNdx = StringTools.parseInt(Tx,-1);
            if (tireNdx >= 0) {
                return tireNdx;
            }
        } else
        if (Tx.length() == 3) {
            // -- "[T]aax"
            int axleNdx = StringTools.parseInt(Tx.substring(0,2),-1);
            int tireNdx = StringTools.parseInt(Tx.substring(2)  ,-1);
            if ((axleNdx >= 0) && (tireNdx >= 0)) {
                return GetTireIndex(axleNdx, tireNdx);
            }
        } else
        if ((Tx.length() >= 4) && ((Tx.charAt(2) == '_') || (Tx.charAt(2) == '-'))) {
            // -- "[T]aa_x", "[T]aa-x
            int axleNdx = StringTools.parseInt(Tx.substring(0,2),-1);
            int tireNdx = StringTools.parseInt(Tx.substring(3),-1);
            if ((axleNdx >= 0) && (tireNdx >= 0)) {
                return GetTireIndex(axleNdx, tireNdx);
            }
        }
        return -1;
    }

    /**
    *** Parses the tire states from the specified String and returns them in the specified Map instance.
    *** May return null if the specified Map instance is null, and nothing is parsed from the specified
    *** String.
    *** @param s         The String containing the tire state list information
    *** @param map       The map containing the tire index and corresponding TireState
    *** @param tempOnly  True if the String contains temperature values only
    *** @return The map containing the tire index and corresponding TireState
    **/
    public static Map<Integer,TireState> ParseTireState(String s, boolean tempOnly, Map<Integer,TireState> map)
    {

        /* validate String */
        if (StringTools.isBlank(s)) {
            // -- invalid String, return map as-is
            return map;
        }
        s = s.trim().toUpperCase();

        /* pressure/temperature properties */
        if (s.startsWith("T") || (s.indexOf("=") >= 0)) {
            // -- Pressure   : "T01=25/30,100 T01=27/35 ..."
            // -- Temperature: "T01=100 T01=103 ..."
            RTProperties rtp = new RTProperties(s);
            for (Object K : rtp.getPropertyKeys()) {
                String Ks = (String)K;
                String Vs = rtp.getString(Ks,"");
                int tNdx = ParseTireIndex(Ks);
                if (tNdx >= 0) {
                    // -- tire index is valid
                    TireState ts = (map != null)? map.get(new Integer(tNdx)) : null;
                    if (ts == null) {
                        // -- new TireState
                        ts = new TireState().setTireIndex(tNdx);
                        if (map == null) { map = new HashMap<Integer,TireState>(); }
                        map.put(new Integer(tNdx), ts);
                    }
                    TireState.UpdateTireState(ts, Vs, tempOnly);
                }
            }
        } else 
        if (s.indexOf(",") >= 0) {
            // -- "25,27,30,35,..."
            String P[] = StringTools.split(s,',');
            for (int tNdx = 0; (tNdx < P.length) && (tNdx < GetMaximumTires()); tNdx++) {
                String Vs = P[tNdx];
                TireState ts = (map != null)? map.get(new Integer(tNdx)) : null;
                if (ts == null) {
                    ts = new TireState().setTireIndex(tNdx);
                    if (map == null) { map = new HashMap<Integer,TireState>(); }
                    map.put(new Integer(tNdx), ts);
                }
                TireState.UpdateTireState(ts, Vs, tempOnly);
            }
        } else {
            // -- unable to recognize format 
        }

        /* return map */
        return map;

    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
    *** Gets the TireState for the specified tire index
    **/
    public static TireState GetTireStateForIndex(Map<Integer,TireState> tsm, int tireNdx)
    {
        if (!ListTools.isEmpty(tsm) && (tireNdx >= 0)) {
            return tsm.get(new Integer(tireNdx));
        }
        return null;
    }

    /**
    *** Gets the TireState for the specified tire index
    **/
    public static TireState GetTireStateForIndex(Map<Integer,TireState> tsm, int axleNdx, int axleTireNdx)
    {
        int tireNdx = GetTireIndex(axleNdx, axleTireNdx);
        return TireState.GetTireStateForIndex(tsm, tireNdx);
    }

    // --------------------------------

    /**
    *** Gets the TireState for the specified tire index
    **/
    public static TireState GetTireStateForIndex(TireState tsa[], int tireNdx)
    {
        if (!ListTools.isEmpty(tsa) && (tireNdx >= 0)) {
            for (TireState ts : tsa) {
                if ((ts != null) && (ts.getTireIndex() == tireNdx)) {
                    return ts;
                }
            }
        }
        return null;
    }

    /**
    *** Gets the TireState for the specified tire index
    **/
    public static TireState GetTireStateForIndex(TireState tsa[], int axleNdx, int axleTireNdx)
    {
        int tireNdx = GetTireIndex(axleNdx, axleTireNdx);
        return TireState.GetTireStateForIndex(tsa, tireNdx);
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
    *** Gets a String representation of the combined TireState array
    **/
    public static String GetTireStatePropertyString(Map<Integer,TireState> tsm)
    {
        if (!ListTools.isEmpty(tsm)) {
            // -- "T00_0=500/500,10 T00_1=500/500,10 ..."
            StringBuffer sb = new StringBuffer();
            for (Integer ti : tsm.keySet()) {
                TireState ts = tsm.get(ti);
                if (TireState.IsValid(ts)) {
                    if (sb.length() > 0) { sb.append(" "); }
                    ts.toKeyString(sb,true,-1/*dftNdx*/);
                    sb.append("=");
                    ts.getPressureString(sb);
                    if (ts.hasActualTemperature()) {
                        sb.append(",");
                        ts.getTemperatureString(sb);
                    }
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
    *** Gets a String representation of the combined TireState array
    **/
    public static String GetTireStatePropertyString(TireState tsa[])
    {
        if (!ListTools.isEmpty(tsa)) {
            // -- "T00_0=500/500,10 T00_1=500/500,10 ..."
            StringBuffer sb = new StringBuffer();
            for (TireState ts : tsa) {
                if (TireState.IsValid(ts)) {
                    if (sb.length() > 0) { sb.append(" "); }
                    ts.toKeyString(sb,true,-1/*dftNdx*/);
                    sb.append("=");
                    ts.getPressureString(sb);
                    if (ts.hasActualTemperature()) {
                        sb.append(",");
                        ts.getTemperatureString(sb);
                    }
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    private int    tireNdx          = -1;
    private int    axleNdx          = -1;
    private int    axleTireNdx      = -1;

    private double actualPressure   = INVALID_PRESSURE;
    private double preferredPress   = INVALID_PRESSURE;
    private double temperature      = INVALID_TEMPERATURE;

    /**
    *** Constructor
    **/
    public TireState()
    {
        super();
        this.clearTireIndex();
        this.clearState();
    }

    /**
    *** Constructor
    **/
    public TireState(int tireNdx)
    {
        super();
        this.setTireIndex(tireNdx);
        this.clearState();
    }

    /**
    *** Constructor
    **/
    public TireState(int axleNdx, int tireNdx)
    {
        super();
        this.setTireIndex(axleNdx, tireNdx);
        this.clearState();
    }

    // ------------------------------------------------------------------------

    /**
    *** Clears the tire index
    **/
    public TireState clearTireIndex()
    {
        this.tireNdx     = -1;
        this.axleNdx     = -1;
        this.axleTireNdx = -1;
        return this;
    }

    /**
    *** Sets the tire index
    **/
    public TireState setTireIndex(int tireNdx)
    {
        this.tireNdx     = tireNdx;
        this.axleNdx     = GetAxleIndex(tireNdx);
        this.axleTireNdx = GetAxleTireIndex(tireNdx);
        return this;
    }

    /**
    *** Sets the tire index
    **/
    public TireState setTireIndex(int axleNdx, int tireNdx)
    {
        if (axleNdx < 0) {
            this.setTireIndex(tireNdx);
        } else {
            this.axleNdx     = axleNdx;
            this.axleTireNdx = tireNdx;
            this.tireNdx     = GetTireIndex(this.axleNdx, this.axleTireNdx);
        }
        return this;
    }

    // --------------------------------

    /**
    *** Returns true if this instance has an axle index
    **/
    public boolean hasAxleIndex()
    {
        return (this.axleNdx >= 0)? true : false;
    }

    /**
    *** Gets the axle index, or -1 if no axle index is defined
    **/
    public int getAxleIndex()
    {
        return this.axleNdx;
    }

    // --------------------------------

    /**
    *** Returns true if this instance has an axle/tire index
    **/
    public boolean hasAxleTireIndex()
    {
        return (this.axleTireNdx >= 0)? true : false;
    }

    /**
    *** Gets the axle/tire index, or -1 if no axle index is defined
    **/
    public int getAxleTireIndex()
    {
        return this.axleTireNdx;
    }

    // --------------------------------

    /**
    *** Returns true if this instance has a tire index
    **/
    public boolean hasTireIndex()
    {
        return (this.tireNdx >= 0)? true : false;
    }

    /**
    *** Gets the axle index, or -1 if no axle index is defined
    **/
    public int getTireIndex()
    {
        return this.tireNdx;
    }

    // ------------------------------------------------------------------------

    /**
    *** Clears the tire pressure/temperature state
    **/
    public TireState clearState()
    {
        this.setActualPressure(INVALID_PRESSURE);
        this.setPreferredPressure(INVALID_PRESSURE); 
        this.setActualTemperature(INVALID_TEMPERATURE); 
        return this;
    }

    /**
    *** Set tire pressure/temperature information
    **/
    public TireState updateState(String s, boolean tempOnly)
    {
        TireState.UpdateTireState(this, s, tempOnly);
        return this;
    }

    // ------------------------------------------------------------------------

    /**
    *** Gets the Actual Pressure from the specified TireState
    *** @param ts   The TireState from which the actual pressure will be returned
    *** @return The actual tire pressure
    **/
    public static double GetActualPressure(TireState ts)
    {
        if ((ts != null) && ts.hasActualPressure()) {
            return ts.getActualPressure();
        } else {
            return TireState.INVALID_PRESSURE;
        }
    }

    /**
    *** Returns true if this instance has a valid actual pressure
    **/
    public boolean hasActualPressure()
    {
        return TireState.IsValidPressure(this.getActualPressure());
    }

    /**
    *** Sets the actual tire pressure (in kPa)
    **/
    public void setActualPressure(double press)
    {
        this.actualPressure = TireState.IsValidPressure(press)? press : INVALID_PRESSURE;
    }

    /**
    *** Sets the actual tire pressure
    **/
    public double getActualPressure()
    {
        return this.actualPressure;
    }

    // ------------------------------------------------------------------------

    /**
    *** Gets the Preferred Pressure from the specified TireState
    *** @param ts   The TireState from which the preferred pressure will be returned
    *** @return The preferred tire pressure
    **/
    public static double GetPreferredPressure(TireState ts)
    {
        if ((ts != null) && ts.hasPreferredPressure()) {
            return ts.getPreferredPressure();
        } else {
            return TireState.INVALID_PRESSURE;
        }
    }

    /**
    *** Returns true if this instance has a valid preferred pressure
    **/
    public boolean hasPreferredPressure()
    {
        return TireState.IsValidPressure(this.getPreferredPressure());
    }

    /**
    *** Sets the preferred tire pressure (in kPa)
    **/
    public void setPreferredPressure(double press)
    {
        this.preferredPress = TireState.IsValidPressure(press)? press : INVALID_PRESSURE;
    }

    /**
    *** Sets the preferred tire pressure
    **/
    public double getPreferredPressure()
    {
        return this.preferredPress;
    }

    // ------------------------------------------------------------------------

    /**
    *** Gets the ratio of actual pressure divided by preferred pressure, minus one.
    *** Return will be:<br>
    ***     Double.NaN if the actual or preferred pressure is not available.<br>
    ***     < 0 if the pressure is low
    ***     > 0 if the pressure is high
    **/
    public double getPressureRatio(double dftPrefPress)
    {

        /* get actual pressure */
        double actPress = this.getActualPressure();
        if (!TireState.IsValidPressure(actPress)) {
            // -- no actual pressure value
            return Double.NaN;
        }

        /* get preferred pressure */
        double prefPress = this.getPreferredPressure();
        if (!TireState.IsValidPressure(prefPress) || (prefPress <= 0.0)) {
            if (TireState.IsValidPressure(dftPrefPress) && (dftPrefPress > 0.0)) {
                prefPress = dftPrefPress;
            } else {
                // -- no preferred pressure to compare against
                return Double.NaN;
            }
        }
        // -- prefPress is > 0.0 (no divide by zero errors)

        /* pressure ratio */
        return (actPress / prefPress) - 1.0; // ie. (90/100) - 1 = -0.10

    }

    // --------------------------------

    /**
    *** Returns true if the tire pressure is low
    **/
    public boolean isPressureLow(double pct, double dftPrefPress)
    {

        /* get ratio */
        double ratio = this.getPressureRatio(dftPrefPress);
        if (Double.isNaN(ratio)) {
            // -- unable to determine ratio
            return false;
        } else
        if (ratio > 0.0) {
            // -- high pressure
            return false;
        } else
        if (ratio == 0.0) {
            // -- exactly at preferred pressure
            return false;
        }

        /* pressure is low */
        if (Math.abs(ratio) < Math.abs(pct)) {
            // -- but not too low
            return false;
        }

        /* pressure is too low */
        return true;

    }

    /**
    *** Gets the first TireState instance with low pressure
    **/
    public static Collection<TireState> GetLowPressureTireState(Map<Integer,TireState> tsm, double pct, double dftPrefPress)
    {
        if (!ListTools.isEmpty(tsm)) {
            Vector<TireState> list = null;
            for (Integer tsi : tsm.keySet()) {
                TireState ts = tsm.get(tsi);
                if ((ts != null) && ts.isPressureLow(pct,dftPrefPress)) {
                    if (list == null) { list = new Vector<TireState>(); }
                    list.add(ts);
                }
            }
            return list;
        } else {
            return null;
        }
    }

    /**
    *** Gets the first TireState instance with low pressure
    **/
    public static Collection<TireState> GetLowPressureTireState(TireState tsa[], double pct, double dftPrefPress)
    {
        if (!ListTools.isEmpty(tsa)) {
            Vector<TireState> list = null;
            for (TireState ts : tsa) {
                if ((ts != null) && ts.isPressureLow(pct,dftPrefPress)) {
                    if (list == null) { list = new Vector<TireState>(); }
                    list.add(ts);
                }
            }
            return list;
        } else {
            return null;
        }
    }

    // --------------------------------

    /**
    *** Returns true if the tire pressure is low
    **/
    public boolean isPressureHigh(double pct, double dftPrefPress)
    {

        /* get ratio */
        double ratio = this.getPressureRatio(dftPrefPress);
        if (Double.isNaN(ratio)) {
            // -- unable to determine ratio
            return false;
        } else
        if (ratio < 0.0) {
            // -- low pressure
            return false;
        } else
        if (ratio == 0.0) {
            // -- exactly at preferred pressure
            return false;
        }

        /* pressure is high */
        //Print.logInfo("Compare ratio: "+Math.abs(ratio)+" <= "+Math.abs(pct));
        if (Math.abs(ratio) < Math.abs(pct)) {
            // -- but not too high
            return false;
        }

        /* pressure is too high */
        return true;

    }

    /**
    *** Gets the first TireState instance with high pressure
    **/
    public static Collection<TireState> GetHighPressureTireState(Map<Integer,TireState> tsm, double pct, double dftPrefPress)
    {
        if (!ListTools.isEmpty(tsm)) {
            Vector<TireState> list = null;
            for (Integer tsi : tsm.keySet()) {
                TireState ts = tsm.get(tsi);
                if ((ts != null) && ts.isPressureHigh(pct,dftPrefPress)) {
                    if (list == null) { list = new Vector<TireState>(); }
                    list.add(ts);
                }
            }
            return list;
        } else {
            return null;
        }
    }

    /**
    *** Gets the first TireState instance with high pressure
    **/
    public static Collection<TireState> GetHighPressureTireState(TireState tsa[], double pct, double dftPrefPress)
    {
        if (!ListTools.isEmpty(tsa)) {
            Vector<TireState> list = null;
            for (TireState ts : tsa) {
                if ((ts != null) && ts.isPressureHigh(pct,dftPrefPress)) {
                    if (list == null) { list = new Vector<TireState>(); }
                    list.add(ts);
                }
            }
            return list;
        } else {
            return null;
        }
    }

    // ------------------------------------------------------------------------

    /**
    *** Gets the Actual Temperature from the specified TireState
    *** @param ts   The TireState from which the temperature will be returned
    *** @return The tire temperature
    **/
    public static double GetActualTemperature(TireState ts)
    {
        if ((ts != null) && ts.hasActualTemperature()) {
            return ts.getActualTemperature();
        } else {
            return TireState.INVALID_TEMPERATURE;
        }
    }

    /**
    *** Returns true if this instance has a valid temperature
    **/
    public boolean hasActualTemperature()
    {
        return IsValidTemperature(this.getActualTemperature());
    }

    /**
    *** Sets the actual tire temperature (in C)
    **/
    public void setActualTemperature(double C)
    {
        this.temperature = TireState.IsValidTemperature(C)? C : INVALID_TEMPERATURE;
    }

    /**
    *** Sets the actual tire temperature (in C)
    **/
    public double getActualTemperature()
    {
        return this.temperature;
    }

    // --------------------------------

    /**
    *** Returns true if the tire pressure is low
    **/
    public boolean isTemperatureHigh(double highC)
    {
        if (!IsValidTemperature(highC)) {
            return false;
        } else
        if (!this.hasActualTemperature()) {
            return false;
        } else {
            double C = this.getActualTemperature();
            return (C > highC)? true : false;
        }
    }

    /**
    *** Gets the first TireState instance with high pressure
    **/
    public static Collection<TireState> GetHighTemperatureTireState(Map<Integer,TireState> tsm, double highC)
    {
        if (!ListTools.isEmpty(tsm) && IsValidTemperature(highC)) {
            Vector<TireState> list = null;
            for (Integer tsi : tsm.keySet()) {
                TireState ts = tsm.get(tsi);
                if ((ts != null) && ts.isTemperatureHigh(highC)) {
                    if (list == null) { list = new Vector<TireState>(); }
                    list.add(ts);
                }
            }
            return list;
        } else {
            return null;
        }
    }

    /**
    *** Gets the first TireState instance with high temperature
    **/
    public static Collection<TireState> GetHighTemperatureTireState(TireState tsa[], double highC)
    {
        if (!ListTools.isEmpty(tsa) && IsValidTemperature(highC)) {
            Vector<TireState> list = null;
            for (TireState ts : tsa) {
                if ((ts != null) && ts.isTemperatureHigh(highC)) {
                    if (list == null) { list = new Vector<TireState>(); }
                    list.add(ts);
                }
            }
            return list;
        } else {
            return null;
        }
    }

    // ------------------------------------------------------------------------

    /**
    *** Returns true if the specified TireState is valid
    **/
    public static boolean IsValid(TireState ts)
    {
        return ((ts != null) && ts.isValid())? true : false;
    }

    /**
    *** Returns trus if this instance has a defined valid pressure or temperature
    **/
    public boolean isValid()
    {
        if (this.hasActualPressure()) {
            return true;
        } else
        if (this.hasPreferredPressure()) {
            return true;
        } else
        if (this.hasActualTemperature()) {
            return true;
        } else {
            return false;
        }
    }

    // ------------------------------------------------------------------------

    /**
    *** Gets a Key name for this instance, representing the Tire Index
    *** @param sb  The StringBuffer to which the Key name will be appended
    *** @return The StringBuffer containing the Key name
    **/
    public StringBuffer toKeyString(StringBuffer sb, boolean perAxle, int dftNdx)
    {
        // -- tire index
        int tireNdx     = -1;
        int axleNdx     = -1;
        int axleTireNdx = -1;
        if (this.hasTireIndex()) {
            tireNdx     = this.getTireIndex();
            axleNdx     = this.getAxleIndex();
            axleTireNdx = this.getAxleTireIndex();
        } else
        if (dftNdx >= 0) {
            tireNdx     = dftNdx;
            axleNdx     = GetAxleIndex(dftNdx);
            axleTireNdx = GetAxleTireIndex(dftNdx);
        }
        // -- assemble key
        if (sb == null) {
            sb = new StringBuffer();
        }
        sb.append("T");
        if (tireNdx < 0) {
            // -- no nothing
        } else
        if (perAxle) {
            // -- "T01-2"
            sb.append(StringTools.format(axleNdx,"00")).append("-").append(axleTireNdx);
        } else {
            // -- "T09"
            sb.append(StringTools.format(tireNdx,"00"));
        }
        return sb;
    }

    /**
    *** Gets a Key name for this instance, representing the Tire Index
    *** @param sb  The StringBuffer to which the Key name will be appended
    *** @return The StringBuffer containing the Key name
    **/
    public String toKeyString(boolean perAxle, int dftNdx)
    {
        return this.toKeyString(null,perAxle,dftNdx).toString();
    }

    // --------------------------------

    /** 
    *** Gets a String representation of the pressure values in the format "Actual/Preferred".
    *** Returns only the "Actual" pressure if the Preferred pressure is not available.
    **/
    public StringBuffer getPressureString(String prefix, StringBuffer sb)
    {
        if (sb == null) {
            sb = new StringBuffer();
        }
        if (this.hasActualPressure() || this.hasPreferredPressure()) {
            if (prefix != null) {
                sb.append(prefix);
            }
            if (this.hasActualPressure()) {
                sb.append(StringTools.format(this.getActualPressure(),"0.0"));
            }
            if (this.hasPreferredPressure()) {
                sb.append("/");
                sb.append(StringTools.format(this.getPreferredPressure(),"0.0"));
            }
        }
        return sb;
    }

    /** 
    *** Gets a String representation of the pressure values in the format "Actual/Preferred".
    *** Returns only the "Actual" pressure if the Preferred pressure is not available.
    **/
    public StringBuffer getPressureString(StringBuffer sb)
    {
        return this.getPressureString(null, sb);
    }

    /** 
    *** Gets a String representation of the pressure values in the format "Actual/Preferred".
    *** Returns only the "Actual" pressure if the Preferred pressure is not available.
    **/
    public String getPressureString(String prefix)
    {
        return this.getPressureString(prefix,null).toString();
    }

    // --------------------------------

    /** 
    *** Gets a String representation of the temperature value.
    **/
    public StringBuffer getTemperatureString(String prefix, StringBuffer sb)
    {
        if (sb == null) {
            sb = new StringBuffer();
        }
        if (this.hasActualTemperature()) {
            if (prefix != null) {
                sb.append(prefix);
            }
            sb.append(StringTools.format(this.getActualTemperature(),"0.0"));
        }
        return sb;
    }

    /** 
    *** Gets a String representation of the temperature value.
    **/
    public StringBuffer getTemperatureString(StringBuffer sb)
    {
        return this.getTemperatureString(null, sb);
    }

    /** 
    *** Gets a String representation of the temperature value.
    **/
    public String getTemperatureString(String prefix)
    {
        return this.getTemperatureString(prefix,null).toString();
    }

    // --------------------------------

    /**
    *** Return a String representation of this instance, in the following format: <br>
    ***     Taa_t=PRESSURE/PREFERRED,TEMPERATURE
    *** Where:<br>
    ***     aa          is the Axle index <br>
    ***     t           is the Axle/Tire index <br>
    ***     PRESSURE    is the actual tire pressure <br>
    ***     PREFERRED   is the preferred tire pressure (optional) <br>
    ***     TEMPERATURE is the tire temperature (optional) <br>
    **/
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        // --
        this.toKeyString(sb,true,-1);
        sb.append("=");
        // --
        this.getPressureString(sb);
        if (this.hasActualTemperature()) {
            sb.append(",");
            this.getTemperatureString(sb);
        }
        // --
        return sb.toString();
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

}
