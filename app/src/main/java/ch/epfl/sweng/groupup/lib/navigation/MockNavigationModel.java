package ch.epfl.sweng.groupup.lib.navigation;

import android.location.Location;

public class MockNavigationModel implements NavigationModelInterface {
    @Override
    public String findRoute(Location origin, Location destination) {
        return "{\n" +
                "   \"geocoded_waypoints\" : [\n" +
                "      {\n" +
                "         \"geocoder_status\" : \"OK\",\n" +
                "         \"place_id\" : \"ChIJ6TJOVDIujEcRyJdGTzQyfeI\",\n" +
                "         \"types\" : [ \"premise\" ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"geocoder_status\" : \"OK\",\n" +
                "         \"place_id\" : \"ChIJs65SeQgKkEcRzYtfWRzGTXQ\",\n" +
                "         \"types\" : [ \"street_address\" ]\n" +
                "      }\n" +
                "   ],\n" +
                "   \"routes\" : [\n" +
                "      {\n" +
                "         \"bounds\" : {\n" +
                "            \"northeast\" : {\n" +
                "               \"lat\" : 47.4598875,\n" +
                "               \"lng\" : 8.541699600000001\n" +
                "            },\n" +
                "            \"southwest\" : {\n" +
                "               \"lat\" : 46.4739663,\n" +
                "               \"lng\" : 6.6322734\n" +
                "            }\n" +
                "         },\n" +
                "         \"copyrights\" : \"Map data Â©2017 GeoBasis-DE/BKG (Â©2009), Google\",\n" +
                "         \"legs\" : [\n" +
                "            {\n" +
                "               \"distance\" : {\n" +
                "                  \"text\" : \"224 km\",\n" +
                "                  \"value\" : 223958\n" +
                "               },\n" +
                "               \"duration\" : {\n" +
                "                  \"text\" : \"2 hours 32 mins\",\n" +
                "                  \"value\" : 9148\n" +
                "               },\n" +
                "               \"end_address\" : \"Bahnhofpl. 2, 8001 ZÃ¼rich, Switzerland\",\n" +
                "               \"end_location\" : {\n" +
                "                  \"lat\" : 47.3768986,\n" +
                "                  \"lng\" : 8.541699600000001\n" +
                "               },\n" +
                "               \"start_address\" : \"Saint-FranÃ§ois, tunnel O, 1003 Lausanne, Switzerland\",\n" +
                "               \"start_location\" : {\n" +
                "                  \"lat\" : 46.5196535,\n" +
                "                  \"lng\" : 6.6322734\n" +
                "               },\n" +
                "               \"steps\" : [\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.2 km\",\n" +
                "                        \"value\" : 162\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 37\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 46.5193531,\n" +
                "                        \"lng\" : 6.6343376\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Head \\u003cb\\u003esoutheast\\u003c/b\\u003e on \\u003cb\\u003ePlace Saint-FranÃ§ois\\u003c/b\\u003e/\\u003cb\\u003eRoute 9\\u003c/b\\u003e\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"yz|zGujng@FYDS@GFm@Ba@ReDJqB\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.5196535,\n" +
                "                        \"lng\" : 6.6322734\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.2 km\",\n" +
                "                        \"value\" : 214\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 44\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 46.5190763,\n" +
                "                        \"lng\" : 6.6370853\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Continue straight onto \\u003cb\\u003eAvenue Benjamin-Constant\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"straight\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"}x|zGswng@@Q@e@@WBY?EPyBJuA@MJ_ABe@Ba@?i@Ge@\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.5193531,\n" +
                "                        \"lng\" : 6.6343376\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"56 m\",\n" +
                "                        \"value\" : 56\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 16\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 46.5194767,\n" +
                "                        \"lng\" : 6.63727\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"\\u003cb\\u003eAvenue Benjamin-Constant\\u003c/b\\u003e turns slightly \\u003cb\\u003eleft\\u003c/b\\u003e and becomes \\u003cb\\u003ePlace Benjamin-Constant\\u003c/b\\u003e\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"gw|zGyhog@CKEMGGCEUKKAG?IHEF\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.5190763,\n" +
                "                        \"lng\" : 6.6370853\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"55 m\",\n" +
                "                        \"value\" : 55\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 16\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 46.5198243,\n" +
                "                        \"lng\" : 6.6367881\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Continue onto \\u003cb\\u003eRue Saint-Pierre\\u003c/b\\u003e\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"wy|zG}iog@Wr@SZKHKD\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.5194767,\n" +
                "                        \"lng\" : 6.63727\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.3 km\",\n" +
                "                        \"value\" : 314\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 82\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 46.5218661,\n" +
                "                        \"lng\" : 6.6389779\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Continue onto \\u003cb\\u003eRue Caroline\\u003c/b\\u003e\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"{{|zG}fog@M?IAKIu@_@w@]SEi@YUa@MYIQCOEe@Es@Ea@Ic@I[GOIIUQKEMCE?Y@O@\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.5198243,\n" +
                "                        \"lng\" : 6.6367881\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.1 km\",\n" +
                "                        \"value\" : 136\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 22\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 46.5216523,\n" +
                "                        \"lng\" : 6.6401651\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eAvenue de BÃ©thusy\\u003c/b\\u003e/\\u003cb\\u003eRoute 1\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"uh}zGstog@WQIGGKS]@YBUDOFMNSFEl@q@NO\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.5218661,\n" +
                "                        \"lng\" : 6.6389779\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"2.4 km\",\n" +
                "                        \"value\" : 2437\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"5 mins\",\n" +
                "                        \"value\" : 304\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 46.5402487,\n" +
                "                        \"lng\" : 6.6524464\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eleft\\u003c/b\\u003e onto \\u003cb\\u003eRue du Bugnon\\u003c/b\\u003e/\\u003cb\\u003eRoute 1\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eContinue to follow Route 1\\u003c/div\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"ig}zGa|og@@o@ASCQGOGKGIKGICKAkAGi@CuBGQAG?M@QDMBKFKDOHOJOLSHODKBK?KASEyBm@e@Kk@OSE}AUkB[o@IMAMCg@I]I[Ke@Oe@O]SUMSOIIOQIG]g@e@y@yAgCsEaIe@u@EIGGMIa@Qg@S[OICMCQCM?O?_@@M@UBA@A?[HMDK@S?_CA?A?A?A?A?A?AA??A?A?AA??AA??AA??AA??AA?A?A?A?A?A??@A??@A??@A??@A@?@?@A??@YIUKOKUQOMMOIKIK?AEIGOCKCKAICQAKEu@Ea@AQAEAGACACCCICa@C]AQOGCu@SiCy@o@Uc@YUQSSAAe@i@}@iA]e@o@q@c@c@_@]]Y]Y_BqAmAaAcA{@kAeAQOeAaAy@s@Y[MMOSUYMSKQ\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.5216523,\n" +
                "                        \"lng\" : 6.6401651\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.6 km\",\n" +
                "                        \"value\" : 583\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 33\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 46.5384421,\n" +
                "                        \"lng\" : 6.6593588\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Take the ramp to \\u003cb\\u003eSimplon\\u003c/b\\u003e/\\u003cb\\u003eGd-St-Bernard\\u003c/b\\u003e/\\u003cb\\u003eFribourg\\u003c/b\\u003e\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"q{`{Gyhrg@Iq@GYEWAUAY?S?W?UDe@Fg@F[Nq@tBaJBIFk@`@sARs@`AaDT_APq@T{@Jg@XcA?E@Q\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.5402487,\n" +
                "                        \"lng\" : 6.6524464\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"18.1 km\",\n" +
                "                        \"value\" : 18116\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"11 mins\",\n" +
                "                        \"value\" : 645\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 46.4762727,\n" +
                "                        \"lng\" : 6.858133700000001\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Merge onto \\u003cb\\u003eA9\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eToll road\\u003c/div\\u003e\",\n" +
                "                     \"maneuver\" : \"merge\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"gp`{G_tsg@l@uBL_@HW^qATy@XeAZgA^oAv@iC@Gf@kBl@mCf@aCTuANaAVsBJgAH}@Fu@LeBLgBHqAJeBJuAJmALwAHw@LeAL{@`@_CXuAZsAV_A`@sAX}@Tq@Tq@v@kBrAeCf@{@r@eAh@q@zAaBl@s@vAiAdCeBd@WXO@CTIfCeAzAm@zDiAzBs@|Ac@hC{@pA[~FmBx@WLEj@QbF_Bn@StEqBrByA@?hAy@fBeBb@a@jAqA~@mAd@u@z@qAd@_Ax@_BbA{Bh@}Al@eBTy@J]XiAJWJg@XmAXcBT}ANoAVyBPkBRyBBe@BYB_@XuDNmBTyBHy@DUBS@I?AJk@F_@PcANu@P_AXoARu@Le@~@uCHUDQPg@d@iAN_@Re@FM\\\\w@dB}Cb@m@\\\\i@dAuAn@s@l@q@lBcBfCoBBCj@c@bAy@TURQf@i@n@s@z@iAj@}@n@iAnByDNYBEN_@`BuC`@o@l@y@lAqA|@{@rB_BrCuBpAeAl@m@|@cAr@y@v@eAv@qAr@mAR]P[jBkDpC{E`BuCvAgCR[vAkC`AgB`AkB`@}@f@iAl@}Al@gBj@uBNo@He@Jq@He@X_CR_CFuBDkCAiCImBSkDSgCy@{ISeCMsCO_DGiAImBGwBCaDCaD@yCFmCDw@?GDo@J_BRkBViBf@_CZiANi@Ts@f@wAd@gAfAeCt@oB`@iAb@}AXgATmANgAP}ALuAJ}Ab@cJX}DTwBXaBZ}A`@uAh@yAb@gAp@iAp@gAz@eAl@s@t@_A`AwA~@aBd@eAl@aBhAkEl@gDFa@@ED]Hs@J_Ab@gDh@_FRuANgA@K@Kd@gEt@{EHc@F_@XsAbA{FDUDO\\\\}A`@mARm@r@sCpAiEX}@`AsCpAsDr@oBnAkD\\\\aAJYLYFSJW`CeHRm@Rm@~@{C~@sDXgA^aBx@eETgADWBKFYr@oDXuA`@_BR{@r@cCl@sBd@qAv@uB^eAl@cBl@gBf@}Al@wB`@gBf@eC^uBNkAZmCLsAFy@TgCR{BFk@BQDc@RwAZ{BVuAl@wC`@{Ab@yAp@sBfBiE^y@dAcCpJ_Tp@aBj@_BX{@Le@XeAJc@RaANs@N}@TaBHu@Fg@NiBRmCTqCVeCLgAXoBXcBTsAV{ARqAVkBPaBPsBHgBBa@@a@BqB@oCAgCEkDGeDC}CC}C?iDFeEH{DPeENuCTeDRyBFq@Fq@LkALmAPiBPuBNaBLqBHoBFyB@mB@mBCiBAy@CaAKmCQyDIkAKqBKwBMuCGwBE{CCkB?eA?aB@kABcCBuAB{@DuA^kJB]?MBa@FqBFuC@uA?u@AuBC_BEwAGqAIyAK{AO{ASiBU_BWiB_@wBu@cEu@{D[eBYiBG_@E]Kk@WsBScBOqAOcBKmAQuBIsAOeCGmAIsCCoAAi@Ac@Ae@?s@?q@Aw@B{B@_@DiARaDTeCLcABS\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.5384421,\n" +
                "                        \"lng\" : 6.6593588\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"1.0 km\",\n" +
                "                        \"value\" : 1047\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 56\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 46.4772689,\n" +
                "                        \"lng\" : 6.864951599999999\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"At the interchange \\u003cb\\u003e14-La Veyre\\u003c/b\\u003e, keep \\u003cb\\u003eright\\u003c/b\\u003e and follow signs for \\u003cb\\u003eE27\\u003c/b\\u003e/\\u003cb\\u003eA12\\u003c/b\\u003e toward \\u003cb\\u003eBern\\u003c/b\\u003e/\\u003cb\\u003eFribourg\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eToll road\\u003c/div\\u003e\",\n" +
                "                     \"maneuver\" : \"ramp-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"uktzGinzh@HW?ALa@Hg@ReA`@_Bt@oC^qA`@iAJMLYJSPWDIDCHIJGDCDCHCJAJ?F@L@F@D@D@HDDBHHHJDDBFFLFPBH@HBN@H@J?H@PAJ?HAJCPAFCNEHCHCFKREFEDEBKHIDIBG@O@G@GAG?GAECOEKIOOS[MYM[IU_@gA_@eAoDcKo@iB_@cAkAaCM_@\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.4762727,\n" +
                "                        \"lng\" : 6.858133700000001\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"77.1 km\",\n" +
                "                        \"value\" : 77086\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"42 mins\",\n" +
                "                        \"value\" : 2522\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 46.9490515,\n" +
                "                        \"lng\" : 7.406704200000001\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Continue onto \\u003cb\\u003eA12\\u003c/b\\u003e/\\u003cb\\u003eE27\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eToll road\\u003c/div\\u003e\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"}qtzG}x{h@}@wA_@g@mAsA[]EEi@g@qAsA{@y@eAeA{AaBaAkAaAmAcAwAc@m@g@y@a@w@a@{@Ys@YcAUkAQoAMqAGyA?oAByALaBR}AZyA`@qAb@cAf@aAx@gAfAmAt@s@t@s@r@w@n@w@j@w@`@u@Tc@Tk@Vw@Po@Nq@ReALeALaBD{A?_@@]Ak@Ai@Ck@Eg@Gm@K{@O}@Qw@Qk@Me@[{@a@y@a@u@a@k@UW[]USa@]]S[QSKSIo@Wm@Mc@Im@GeAIaEGsCC{BM{AMkAYcA[y@YsAi@eCoAuBqAqA_AqB{Ae@_@wAaAuAgAsB}A}AiAo@e@k@]gAm@eAa@y@U_ASsAYiBQkASu@MaAUw@WOGSIm@[g@[m@_@e@]sCsBQKQMcBcA_B_Aw@a@oAm@{BaA{DaBq@]aAk@k@_@m@c@[]]]_@c@W[{@oAm@_AeCiEgAcBm@y@q@y@u@w@YYw@u@_@Ys@m@wAeA{@o@s@m@s@q@s@s@y@_AOSs@cAu@kAc@s@cAgBKSMUsAeCm@cA{@qAu@aAY[e@g@s@o@SQc@]s@e@m@]c@SEAwBcAqCkAa@Q_@O}Ao@eF}B]O]OyBaAaDuAuAk@yCsAuB_Ag@SwB_Ao@[mBy@_Aa@cAa@o@W{@Yi@Qg@Ok@OcASc@Is@Km@Ge@Eq@Cw@E_A?_A@w@BkAHkAJo@HmAN_Dd@yBX_ALwALq@DeAFcBFg@@gC?{CGe@Cc@AwAIu@Ga@Ea@EoBUeCe@kBa@qBk@oBk@{Bu@oBm@qCw@}Bq@mCu@_Ck@i@KgAUmB_@aAS_AQ}AUcAM_AM_CY}AO}AMeDSyCOgDI}CCuA@aC?e@?e@?uEFeKLaBB{@?}A?eB?eBA_CAyDIcEMaDKeBIwAIaAGqFa@s@EsAMk@G_AIaBQ}BYiDa@yAUyAS_AO}B_@aCa@aB[}A[{A[_Cg@_B_@}Bi@iFuAgAYk@Qy@UuFeB_DeAyCcA}Ao@wCgA}B_A{Ao@uAm@cAc@]O]OgGuCyAu@wBgAsC{AqC_BkDqBqCcBmBoAkAw@u@e@i@_@yB{AiBqA_Aq@oBwAqAcAoB{Aw@o@qAeAk@e@aAy@o@k@mCaCaDsCmBkBkBgB}C}CcCgCuC}C_DkD_CkCsDeE_@e@aEcFkAyAq@}@}AqBgByBwBuC{B{CaB}BqDgFOWOUcCoDoAqBuBcD}AcCeAeB_A}AwA_CcAcBi@_Am@cAaBwCs@wAi@gAUm@eAkC_@aA]_Aq@wBg@aByByHaA_Du@}BkA{Cw@iBaAuBk@eAcAgBW_@k@}@w@kAuI_L_C_D{AcC{AkCk@iAg@eAqAyCc@kAe@mAOk@Uq@GSQm@_@qAa@wAi@yB]yAMm@UqAYyASqAEYE[UaBQqAQaBK_ASoBc@eFEc@Ga@yB}VsAcOaJgcAIw@Gw@OyA_@}DWgCa@yDa@mDm@iFa@eDIu@Ku@_BcMQuAQ}AYcCSaBW}BQaBO{AM}AI{@UeCUoCM_BO{BMcBCq@K}AAOMuB]wHCg@Cg@G_BMmDGkCGyBGgBEoBIyDCs@CiBOyFOyGKuCIuBMcCAUGaA_@_Gc@eFQ{A]cDYsB[aCg@eDk@iDWwAg@kCgA_FWaAESI[gAgE_@oA{A_Fi@{AUq@c@oAw@sBiAqCqAuCe@cA_AkBaAmB[k@mAwBuBiDkBqCwBsCQUyBqC}@_AaAeAmAmAwAwA{CmCmB}A{@u@QMIECCGEAAGEqCyB{BcByEmDIIGEIG[Uu@m@yBcBaDgCGEyAoAa@]gAcAkBiB_A_AiCsCY]sBiCqBqCaBcCkAkBsA}Bs@qAOYy@}AoAkCy@cBmAmCoBiEq@wAgA_Ck@mAa@w@iBkDS]cAeBoAqB{AwBiB}BoA}AGGqByBsBmBeB{A}C_CiAw@QMEESMi@][QoAq@eCqAyBaA_DiAaC{@kBm@cCs@mEoAMCKCOEwBk@oBg@mD}@sA]qA]aBc@c@M{EoA_Dy@iBc@eCs@_@K]G]IuBm@m@Oq@UoFuAy@UyBm@eBc@kD_A_B_@s@UiD{@oBg@]I[I[IWIgBe@uA_@eCs@gCu@iA]wAa@uCaAaA_@uAg@oC{AqByA_Ay@y@w@q@q@qA}Au@cAu@eA[g@aA_Bi@eAWi@Wi@oA{CgAaDq@yB]gAq@aC]iAgBmGy@cCaAoC[y@a@cAk@gAw@{AW_@KOWa@k@y@aAiAKK}A{AgAu@uAu@oAk@aBg@iAUuASiBKkB@c@@cAHu@Fc@H]F]F_@Fw@R_Cn@yBn@cC|@wBx@c@PoEhB{@ZcAb@aB|@qAp@mBhA_Af@_@T_@VuC~AuAt@oAh@kAf@sAj@iA^w@P_@FoARk@FSBeA@uACk@Cg@Ci@A{@I{AO{@OcAS_AYmCaAQIw@e@YQ[WIGy@s@kBcB}BoB{AqA}@s@{@q@oA{@OIgAs@aAg@aBu@qBm@uAYkBYcAKaAE_AC_A?eBDq@Fu@FkAPOBI@]Hi@Ns@TqA`@wAn@yAx@{AbA{AlA_@\\\\qApAiAhAeBlBeDzDc@`@kAlAqAhAMLMJw@l@e@ZuAz@mAl@u@\\\\s@Vq@Tc@Le@LwAZaANqALsAJi@@wA?s@AsAGcAIgAMSEQCwA[{Cm@uA]yBk@mCo@g@MuBe@sAQaBSyAOs@CoBGcAAoB@wCHcCRuCZgBTe@Fy@NwBZ}Dh@qAN]DsCRsAFS@eCD{B?]?G?S?wAAkDMsCSwBUyBYgB[{AYYGqD}@}DmA_Bi@gBs@y@]yB_Au@]ECcAe@cAe@g@Ue@SOIKEKGuAm@aAc@wCqAeAa@u@Wg@Q}Bm@m@K{AUQCsAO]CuBIcC?uBJg@BS@SB{D\\\\}CVkBLu@FeBF_DBmCG{BMqBSkC]yE_AkMuCu@Oy@QqDy@eAU}A[aBa@qAY{A]uAWiAMwAMq@E{@AgABa@BqC^_Bd@mAb@aAd@q@^s@h@aD`CkCvB}@l@_B`AoAn@_@NUHs@Ty@R{AT}AJkABu@Cw@E}@M}@QoA]cBs@}A}@g@_@OMw@q@kAmAOS]e@a@k@W_@sAcC{@kBy@qBwCuHM[M[_@eAoAgDc@aAo@_B_AcCq@aBk@}A{@{BwAmDkCiGsAmCu@mAyA{BU_@[]mAwAkAkA}@u@mCqB{AaA{A_A}A_Ai@[MIWOmAw@kBkAiBmAkA_AeAcAy@u@MMwAyAe@i@a@o@a@i@eAeBm@eAw@cB[u@g@oAa@mAe@_BiA{E]uB]gCm@qGQiCO}CGeBQsFWaJIoDO}BMuBWyCAQq@{EUuA[}AAEI_@G[AEiAiEy@aCsAgDq@yAuAoCg@aA_DeGwAmCu@wAy@}AaAkBiAuBaBqCe@o@i@w@g@m@e@g@m@k@_As@cAo@e@Wi@Wk@SsA[k@Ku@MoAIi@Ao@@i@@g@DcAJ_@Hg@HoAXaAXoA\\\\YHoA^oAZmAZmARYBw@Fq@D]?oA?kAIw@Is@My@Qg@O]KIEUImAk@s@_@m@a@q@g@i@e@a@_@UWUWa@c@g@o@c@o@}@yAYi@Ym@Q_@}@_C]aA]gASs@i@kBSs@Oi@GUUy@YeAYiAmAgE]mAk@kBGS?AGQWu@Wu@[y@i@oAi@iAi@aAu@oAw@kAuAeBqAsAoBiB}@o@cAo@aCqA_DcBiEaCKG_CoAuEeCQIqDoB}CcBwCeBa@W}@o@o@c@UQi@c@UQMK[UeAcAs@q@{@w@eCmCgC_DeCgDcBeCS]_@m@cFqIgFqIoLyRQ[gAiByAaCwAkCmAwBmAaCsAqCoBmEaAiCyA_EoA_EqAcEo@{BMa@?AOc@qAuEiAsD}@{CgAqD_BqF{AgFq@}BW_AW{@ACUw@Ok@w@kCk@oBw@iCi@kBIWQk@YeAoBsGmAgEa@wAEQQi@q@uBmAuDgCiH[}@iBwE_AyBe@gAkAiC{@gBc@aAw@_Bo@qAg@aAmBoDwAiCa@q@a@q@gAeB{@}AiBaDiAuBw@yAs@sA]s@S_@Sa@u@aBoE{JqBiFaAkCoBeG}AaFm@wB_C{Iw@iDYqAo@uCg@gCm@aDcAcGEWUaB_@cCIa@]oC]mCUmBQcBUsBOeBY}CM_B]kEI_B[kFImBMuCKqCAc@Ac@OsFGiEG}EEsFAYGcHCsEGeFEaGCcFAg@AcAMaPS}LY_H[_Fc@eFs@qG}@oGu@kEYuAuFcXsAeHg@}CO{@AKKo@CUGe@CSUeBQmBQwAMyAMyAMqBKwBOkCIuBI}CMqGKuFG}DIaEGoBEw@?CEm@Ek@QeCSuBQ}AG_@CUCY]{BY{Ae@}Bc@mBm@wBWy@W{@m@eBe@qAi@qAoAiCcAkBcAcBo@aAq@}@kAyAiAqAs@y@s@y@[]q@u@q@u@u@{@q@u@s@{@[_@s@{@o@u@]c@m@y@Ya@k@w@[g@i@y@q@gAk@_Ak@eAk@gAi@eAk@iAk@qAe@cAg@mAe@mAg@sAc@mAa@kA_@qAc@qAa@yA]qA[oA_@wAWqA]}Ae@_CWuAy@gFq@kFK}@[gDW_DQeCG_AKyBIwBK{DGyDEoFG{EEoECoEAg@?g@CgB?[?[C{CAy@Ay@EyF?]Aq@?e@IyJAmB?sC@gDFeBBmA@S?A?QJaCNkCLcBZqDj@eFFi@@EFi@f@qFPyBHyALwCB}ABkCAcDAqBAa@EkEGwDEwBEoBA{AMsDM_EIiBG}AE}@WiFM}BMaCSqCOyAKkAQkBOwAe@uDc@wCa@sBc@}BEQEQACCM?AAE]uAIYCIKa@q@yBc@oAe@mAe@mA}@iBk@gAm@eAm@{@OS?AQSeAqAkAoAoAiAuAcAqAy@m@Yo@[g@Ui@SIEMGqBo@QGEA]KqBs@iAg@gAk@s@c@]Uy@m@sAcAw@i@wAaA{@i@{@c@c@Su@[e@Qs@W_@K]K_@K[IeFmA_Ci@kA[qBk@{Ae@yAk@aBq@wAs@wAy@sA}@yAgAgA{@YUSQCAOO_@Yq@m@yAgAw@i@w@e@aAi@_@SsBcAyCqA{CsAwEqBuCkAa@SqD}AwBaA{BcAeCiAmAk@_@Qy@a@q@]g@[qBwA{AwAu@y@iAwAw@mAmBmDa@{@]s@m@qAq@}Au@eBuByEk@oAq@{Aq@{Am@wAw@eBo@uAIQCEIS[q@kC_GoCeGuB}D_AyAs@iAs@cAY_@ACMOgD}DuCmCcB_BkAiAc@c@OQ{@cAu@aAa@o@w@qAs@uAk@wAQc@Us@]gAUw@WgAKi@SeAQkAU}AKmAM{AGgAI}BEoBCsAIoGAo@EoAKiCKyAIw@Iy@SyAOw@Q{@CMQs@]iA[_ASi@Yo@]o@k@gAg@w@W]_@e@W[e@c@o@g@u@k@e@[g@[}AcA}@w@USOOUUQUMQGKGGQWOUACKOKSQYc@y@Q]]}@Ws@O_@I[GUIUMg@UaAOy@Ki@Mq@OcASoAEUAEIg@Q{@Ki@a@aBCII]YaASo@IUKSKSMWSc@Ua@U[EG]e@MM[YKM_@[YUo@a@[OUKIEMGUGKEUEICICICG?AAE?EAKAIAC?MCmBIG?A?Q?}A?a@?[?Y@g@?U@]?uDBA?I?O?qCBM?W?M?M?Q?[?_@?aB@cGBw@@M?m@?S?\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.4772689,\n" +
                "                        \"lng\" : 6.864951599999999\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"5.2 km\",\n" +
                "                        \"value\" : 5215\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"4 mins\",\n" +
                "                        \"value\" : 218\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 46.969601,\n" +
                "                        \"lng\" : 7.4636643\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Take the exit onto \\u003cb\\u003eA1\\u003c/b\\u003e/\\u003cb\\u003eE25\\u003c/b\\u003e/\\u003cb\\u003eE27\\u003c/b\\u003e toward \\u003cb\\u003eZÃ¼rich\\u003c/b\\u003e/\\u003cb\\u003eBasel\\u003c/b\\u003e/\\u003cb\\u003eInterlaken\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePartial toll road\\u003c/div\\u003e\",\n" +
                "                     \"maneuver\" : \"ramp-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"qvp}G{rel@QCS?e@Ce@A[GSGWKOIMGQOUWIKQWO[M][eA_@{Bq@sECK?CAAWi@a@uBYoAa@aBW}@]kAk@eBKYm@}AcAwBa@u@_@k@uA{BsAsB_AwAOSMQKQyHwKw@mAiCqDaByBmF_Hc@o@q@}@i@w@sEiGk@w@k@y@uB_D_AqAiAcB{@qAU]eBqCg@w@w@uA[m@s@wA_@y@a@}@Se@Um@[w@Y}@a@qAg@mBWcAYuAYyAIe@QgAM}@McAK_AKaAKaAGq@[yCEk@AQCSO{AkAwMYcCS_Ba@oCg@cDe@aCScAUcAQy@c@mBGYMk@YqAS_Ac@qB[_BMo@[eBQaA[kBg@oDKy@MuAKkAMgBEgAC{@AmAAqBB{A@gAFwAHcBFaAJaA\\\\}CRkBRoBN{ALwANsBDcBBwB?oBCoACu@E_AGgAKoAMoAMgAKq@OiAKu@s@_F\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.9490515,\n" +
                "                        \"lng\" : 7.406704200000001\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.9 km\",\n" +
                "                        \"value\" : 929\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 41\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 46.9740161,\n" +
                "                        \"lng\" : 7.4706358\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"At the interchange \\u003cb\\u003e37-Wankdorf\\u003c/b\\u003e, keep \\u003cb\\u003eleft\\u003c/b\\u003e and follow signs for \\u003cb\\u003eE25\\u003c/b\\u003e/\\u003cb\\u003eE27\\u003c/b\\u003e/\\u003cb\\u003eA1\\u003c/b\\u003e toward \\u003cb\\u003eZÃ¼rich\\u003c/b\\u003e/\\u003cb\\u003eBasel\\u003c/b\\u003e/\\u003cb\\u003eBiel\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eToll road\\u003c/div\\u003e\",\n" +
                "                     \"maneuver\" : \"ramp-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"_wt}G{vpl@e@iCOqAIw@ASIiAA_@Co@Ai@AqB?A?m@AU?aA?i@AuAEqAMoAE_@Ie@I]Me@K]GQOa@_@{@U_@SYY[_@a@WUSOGE[OYMUG]Ke@Ik@CM?]?e@D]Dc@L[J[N_@N_@TUJIDGDA@?@OV\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.969601,\n" +
                "                        \"lng\" : 7.4636643\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"5.0 km\",\n" +
                "                        \"value\" : 5014\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"3 mins\",\n" +
                "                        \"value\" : 193\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.01053479999999,\n" +
                "                        \"lng\" : 7.4970107\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Merge onto \\u003cb\\u003eA6\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eToll road\\u003c/div\\u003e\",\n" +
                "                     \"maneuver\" : \"merge\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"sru}Gobrl@u@b@mBlA{Az@sBnAsCdBmCzA{Ar@y@`@y@`@e@Re@TqBp@cA\\\\_AX_AT{@L}@J_AF}@BaA@_BG}@GaAM_AO{Aa@{@Y{@_@{@a@wAy@u@i@y@o@u@q@kAoAs@y@Y_@q@_Ak@}@]m@a@u@g@eAa@}@e@kAIUKWe@yAWy@W_Ac@iBOm@]_BeAsFKk@o@iDKc@CMSaAMo@i@aC]yAe@_BQo@_@gAq@mBWk@_@u@m@kAyAcCqAaBuA}AaByAy@q@_GmEkDiCmAiA[Wi@g@m@m@q@q@q@o@aAgAo@q@gAkA{@aAu@{@y@}@y@_A}@_AeAeA[[Y[_A}@{@u@cBaBoIwHeDsCaCwByAqAyAoAoBaBmByAkCgB}BmAmCiAa@M_@MkAYu@OsASWA}@EG?WAI?\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 46.9740161,\n" +
                "                        \"lng\" : 7.4706358\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"44.6 km\",\n" +
                "                        \"value\" : 44641\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"26 mins\",\n" +
                "                        \"value\" : 1588\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.3108158,\n" +
                "                        \"lng\" : 7.798041\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Continue onto \\u003cb\\u003eA1\\u003c/b\\u003e/\\u003cb\\u003eE25\\u003c/b\\u003e (signs for \\u003cb\\u003eZÃ¼rich\\u003c/b\\u003e/\\u003cb\\u003eBasel\\u003c/b\\u003e/\\u003cb\\u003eKirchberg\\u003c/b\\u003e)\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eToll road\\u003c/div\\u003e\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"yv|}Gigwl@qBFaAHaBPiC^gBROB_@@I@Y@oADkAA{@Eq@IqAQcAUs@QeAc@eAc@MKCAIGOGm@c@_BmAuAsAmA}Ay@iAk@_Ag@_Ac@_Ak@sAk@_BSq@Sq@Sq@e@oBi@uBkGcXuBeJuA}FI_@AGK_@YsAaAcEy@mDy@oD{@sD{@uDeAyE}CaOESCIGYc@cBg@uB{@yCiAcDk@wAg@kASa@Qa@c@{@_AaBiAkB_AoAqAcBkAkAs@s@qAeA{B_BuAw@wBeAaDkAkCq@UGo@M}Cg@cCYaBM}Jc@MAA?M?kEGe@Ci@AE?E?mBCeEM}DQaDWmBSo@ImAWsBg@}Ag@{@[yAq@yAw@iAq@s@e@OIMIoB{AwE_EcDcDmFaG{EmGwCgEyDoGuByDeBoDyAcDkAqCsAgDiA}CwBcGm@gB{@gCkAwDyA{EiA{Dy@yC_AqD[oAYgAESAAI_@kAoE_CsI}ByGsBcFeBeDi@}@_@g@CEs@aAW_@wBiCyDgEsEoEwDkDkCyBwCcCsG}EgBoAeEoCSOMIMIaFaDyDaCq@c@{A_AyDaCsGeE_MsH}AaAiDsB_Ai@kAo@iDcBuBaAu@Wc@Q}Ag@gBe@gASi@KgAOkAMaDIiDDQ@e@BoAPyAP]F_Ft@i@Ha@HE@mAPgANa@F_@D]DYDg@FY@e@D[B]BE?W@S?S@a@@eBEeEU[CUEE?MCWCOCeC]eAOqEo@}Ca@_Ek@iDi@iFu@}BY_@G]GSCSEiDg@sASoCa@mRsCgQoCoEq@eASoCg@gASsEq@qAUmEu@{E{@wBg@cEy@eBYaDq@aFcA}Cs@}Bg@u@SwBg@oAYmAYQEo@OuCu@yA_@qEiAuBm@[I_@KqBk@eCs@sGiBeEqA]Ks@SWIkBk@m@SaCw@iBk@kJ_DqAc@iBo@sAe@oAa@wAg@sAg@uAg@{DuAcC}@{@]{@[_A]eAa@gDqAwDwASI}@[_FmB{@[{Bw@sCaA{@YcBg@uAa@oA]}@U_AUmBc@uAWkB[_AOsAQwAO}@IaAG}@Ei@EuAEsACk@A}@?kB?_ABW@_@@w@ByAFwBNy@Hi@FeC\\\\_ALg@Jc@HuDr@_BZwDx@c@JoAXgFhAaEx@cEr@eDb@mANaBPcCPwAHkAFsCJq@@yAB{EAyDEsEUuBO_Fg@wASq@IsCc@sFiAy@Ue@Kc@My@ScF}Aw@Y}By@{B}@gDyAMGkIaEqHsDsC}Ai@[iCsAiCuAeDeBaCmAcBy@wBcAwB_AuAm@wAm@_Bm@}CgA{Ag@}Ae@sA_@yA_@{A_@o@M_Dm@uCg@gBWyBU}AOwAKyAKwAI}BIwDIgAAeBAw@?wJDiMNqABmEBcE@}DAcCEqBEqBI_BK}AKcBOk@IiAKq@Kq@KwASc@IyAWuAWuBg@gAW_AY{@WcAYeA]e@QOGOGu@WiAc@sAk@iAg@sAq@{Aw@EAu@c@y@g@w@g@u@e@aAs@oAaAgA{@y@u@s@o@q@o@y@{@u@w@mAuAm@u@q@{@e@o@o@{@m@{@o@_As@mAk@}@w@yAq@mAo@oAm@sAg@gAm@uAi@oAo@cBq@kBUu@Yw@eAcDe@_Be@aBgAyD_BkGcAyDI[GU{AiG[eA_AkD_AkDIYaAaDa@sAOa@s@{Bc@gAq@iBoAaDy@gBy@iBq@sAuAgCgBwCYa@Wa@OWuAuBcB}Bg@{@aAwA}A_CuBiDIOg@_ACEKQMW{@eBiByDEKg@oAc@gAaAkC]eAa@uA_@sA]_BO_AQqAMgAGq@I_BGaAMsCEgAIsBMqBWgCYiBg@_Cc@cB_@kAg@kA}AiD?Au@{A[g@MYmEsHkAgBaBcCaB}BmEcGoAiBgA_BuAyBaA_BsAgCqAwC{@qBe@mAu@yBe@wAa@uAg@eBWgACKAEEWGUy@aEu@aEe@}Cq@oEa@yCy@gFO{@Mq@SeAYsAw@oDY}@oBiGKYKYM_@_@_ASi@eAeCe@eAo@}Aq@wA}@kBWc@O]eAwB_BeDqAuCaA}Be@mAk@}As@uBGUEMI]o@uBi@oBS_A_@eBYoAc@mCc@kCWsB_@_DG_AIaAQkCMkBGaAI{AKcDE{@KiD?UEaBAi@IaDM_GMmFKyDS_FS_EOkCKkASeCU{BE[Ec@]kCMcAUqBm@iDs@yDm@mCYsAMg@y@gDi@oB]{@}B}Gu@qBo@}AQa@OYACMWIQ_@}@U]}BuE{@yAOWuAwB}A{BkBiCeDsDOQEEm@q@k@i@iC}BaDeCuCiByBqAmCsA{CuA}Am@{Am@eGuB_@Kg@OkR_GaA_@wCgAiBu@{B}@wFwCoEsC}CeCSQAAQMoAoAcC}Bs@w@o@s@_CsCoDkF]i@QWACGIWc@q@iAgBeDoAiCy@yAw@cBi@mAMYWm@gBmEm@_Bs@kBaAqC_AkCkAsDK[?AOe@yBkHqCyJeCeJm@cCyAwFsEqQsGcWmFwSwBoI_@{AyGkWiAaEU}@cIeYkAgE}@{CeAsDq@aC_B{FaCmI_@sAi@kBSs@i@kBeAuDqAsEcAqD}AuFi@gBy@uC{DuNgByGSs@q@iCaAuDoAeFSw@i@wB_B_HgByHcAyEgB_JG[ACEYiAcG}@gFY}Am@uD_@_Cg@gDc@gDQyACQ_@{CUmBKw@W_C\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.01053479999999,\n" +
                "                        \"lng\" : 7.4970107\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"9.2 km\",\n" +
                "                        \"value\" : 9161\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"5 mins\",\n" +
                "                        \"value\" : 322\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.3047901,\n" +
                "                        \"lng\" : 7.9149695\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Keep \\u003cb\\u003eleft\\u003c/b\\u003e to continue on \\u003cb\\u003eA1\\u003c/b\\u003e, follow signs for \\u003cb\\u003eE35\\u003c/b\\u003e/\\u003cb\\u003eA2\\u003c/b\\u003e/\\u003cb\\u003eZÃ¼rich\\u003c/b\\u003e/\\u003cb\\u003eGotthard\\u003c/b\\u003e/\\u003cb\\u003eLuzern\\u003c/b\\u003e/\\u003cb\\u003eRothrist\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eToll road\\u003c/div\\u003e\",\n" +
                "                     \"maneuver\" : \"keep-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"skw_Hw`rn@Mg@K{@Gi@SyBk@iGKiAWkDUqDMiB]sGM_DQaFImCGwDEsBAgAEoDAqD?kC@aD@_BD{D?q@@o@DsAHyEBo@DoABy@JuCVuFFiAL_CLeBN}BRmCLeB^aE`@}DPgB`@_E^qDHy@v@uHf@mFVqCXuDNmBd@uH@c@Ba@FsAFqBDaBDwBD_C@aBCcHCy@Ay@GyBGoBCq@QqC[eEGw@UyBe@mEWkBK_AgCqQKs@Ko@AIg@mDYyBg@gEOsAYoCYmDQ{BCe@?EAQGoAKcDEuEAcD@wCHeDLoDTwDD}AVaEHmBH{BBc@@c@DeBDeDA_DAyBGgBMyCQ_DWmCYuCy@yHc@cFk@wHS_EGaBGcBMsFIwEEoEEgGGsOAaL?aA@_A?cA?e@?c@?I@uB?I@s@?s@JeD@]FgCBk@?IBu@Du@JoB@]?AB[LqC@U@QNqBJsAD]\\\\_EDYBUD]R{APaBNmA\\\\}B\\\\_Cj@}CD[\\\\aBhA{FHa@xA_Gt@iCfAmDp@{BlC}GfAmCbA{B`B{DjAgCJY\\\\y@hByETq@b@sAtBiHx@gDp@kDh@mDRcBHo@Fg@@I\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.3108158,\n" +
                "                        \"lng\" : 7.798041\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"34.9 km\",\n" +
                "                        \"value\" : 34917\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"21 mins\",\n" +
                "                        \"value\" : 1256\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.4577013,\n" +
                "                        \"lng\" : 8.280250799999999\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Keep \\u003cb\\u003eleft\\u003c/b\\u003e to stay on \\u003cb\\u003eA1\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eToll road\\u003c/div\\u003e\",\n" +
                "                     \"maneuver\" : \"keep-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"}ev_Hq{ho@Fu@ZmDRoDLmDDgBByDA{@?c@AkACkBCaACgAGuAC]?KCc@AOWiDO_BYoC[}BOeAKm@k@_Dy@uDu@kCk@kBa@qAMYAEM]M]m@yAw@kBmAeCcAkBkAqBk@}@QYAAWa@s@eAm@{@eA{A_@i@kA}Aq@}@aK}MwDgF}AcC_A}AcAkBy@eBg@mA{@}Bm@oBw@kCqAeFmBkImAmFy@oDESQu@_@_BaEgQ{AcHqAuG_AaFy@eFy@oFcA}HiA}Kg@cFW}CEg@Ea@mBiUsDqc@E_@Ec@[}Di@eGc@uF]eFWyFKkDI_E?oE@wB?S?A@UBeBBeAHyBHkBRuCRiCBe@BY^iE^iFNoBJyBDmBBeA?sDAcCEwBQcDC_@Cc@C_@a@yFk@mHWsCIoAGkAC_AE_AC_BCkB?aCBuBHmCJmBJ_BR{BVsBb@gDLs@RkAJm@RoAPgAVeBdAuGZqCJ}@VcDFwAJaCFqB?}C@aBCwAAu@EsAIkBKcBOiBQeBS{ACQKk@COACGe@e@gCi@_CGUEOCKq@{BaAwCc@cAUk@Ug@i@gAa@u@gAmB[e@qB{CAAaB{BmBmCaBcCoCuEkCcFgBkDsAoCeDsGQ[O[gBcD}@aB_A_BiAkBeA_Bq@eA{AyBiBeCY_@Y]aBsBiAsAqAsAiBoBs@s@u@s@q@q@sBiBc@c@oCiC}AyA{@y@{@y@sBqBqDkDYWk@i@k@k@yAsAuDmDuHoHIIWYc@c@qBsBOOGGGIcDiDaCiC}BgC}BiCiAqAy@aAqB_CkCgDwDgFU]GKa@i@gEoGqEoH]o@GMYc@sCcF}BiEk@gACGmCcFU_@?A_@m@{@aBaDcFeBaCcBwByAiBwBgC{FwGcAiAwAmBOUQW_AuA_AaB]o@iAcC}@yBq@oBu@eCEMUy@e@oBg@eCk@kDe@iDq@sFg@kEw@wGq@yFGg@AEIi@{@qGYkBs@qE_@_C_AiFq@eD]{AIc@Kc@?CKe@EMu@eDaA}D_BkGw@kCUy@Wy@w@iCQe@AEQk@m@cBMa@M_@s@kBa@oAuBsFmAuCeBgEmBuEYi@s@cBaAqBaBkDkCiFkBcDyAcCiBqCeCqDcAuAkBaCiBwBuD}DkBiBgEuDSQeCqB}FoEi@a@mA}@m@g@AAe@a@_CmB{AoAiB_BgBiBwBgCyAkBq@aAcAyAmBaDy@{A_@s@Ua@wA}C_@y@Yq@cBmE}@cCw@eCiAgE[sA{@{DsAsHm@oEi@}ESwBGk@UcDWwEIeCI}CCcBC_C?_C?sCByBHwDFsA@k@PyEd@_KNyCRmFRgGDcE@}BGeIIyCSkGCe@}@qOK{A?AMqBmBsZIgA]qDEq@c@oDi@yDm@uDCMk@sD}@sEa@sBKq@SiACMQgAa@uBWmAGa@GOMm@a@iCQaA_@cCSgBGm@AGGm@Ea@[gESqFKyFQuQAW?OAe@GiCKoDCaAASCYAYMyBe@_G_@{D[yCOmAGa@Gg@iBeNQuAm@iFYsCWeDQoCI{AKiBKsCCm@Ae@AUAe@Cq@WuMCuAYsMMsEKcCKgBMiBKcAMyAY}BOaAGe@Oy@UsAi@}BU_A}@{Ce@uA]}@s@cBq@{AUg@yAkCaAsAsA}Au@{@aAcAgAgAiCeCiBeBYWcAmAaAkAo@{@e@q@]i@Ye@_@o@GKwBgDSYQY}AmCwC_G_DcGqA}BS]i@y@w@oAiBgCaBsBo@w@o@s@s@w@m@o@e@c@c@c@s@q@_Ay@gA{@gBoAmA}@gCcBwBuA{@i@]SYSo@[UMkAo@{BuAwAaAwAaAgBuAw@o@cB{AMMwAwA_AeAaBqBY[oAcB_AoAw@gAsCsEqF_KqCgFMWAAOYeByCi@{@e@s@_B_Cu@_AqA}A}B}BqBgBw@o@wAaA}@m@c@Yk@]k@YkAk@g@U]MCA[OQG_A_@cA[sCy@kD_AcAYa@K_@KyC{@{Cy@c@K}Ac@iCw@mA]_Be@yAe@WK[KA?mFoB}D_BWKGC]QyDmB_B_AoBkA]W_@WaAq@eBsAuBeBgBcBgAgAi@m@mA{A}AqBsAsBs@kAYe@CGCCO[g@{@i@eAi@iAg@gAQ_@AGIOIUKWq@iB}@kC}@yCu@uCu@iDg@gCWsAKi@SgA]sBSoAUyAg@eDYgBESYmBIg@COEY_@yBIa@Ia@SmAYyAq@gDs@eDq@wCw@}Cq@gCm@uBo@uB}@sCg@aBIUGO?AOc@cBmE{@{B}@{By@uBo@_BWo@aAiCg@uAe@uAw@cCi@gBe@gBACc@kBS}@_@eBc@qCQoAS}AQoBScCOaCU}ECk@\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.3047901,\n" +
                "                        \"lng\" : 7.9149695\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"12.9 km\",\n" +
                "                        \"value\" : 12911\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"8 mins\",\n" +
                "                        \"value\" : 480\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.4136384,\n" +
                "                        \"lng\" : 8.423223699999999\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Keep \\u003cb\\u003eleft\\u003c/b\\u003e at the fork to continue on \\u003cb\\u003eA1\\u003c/b\\u003e/\\u003cb\\u003eA3\\u003c/b\\u003e/\\u003cb\\u003eE60\\u003c/b\\u003e, follow signs for \\u003cb\\u003eZÃ¼rich\\u003c/b\\u003e/\\u003cb\\u003eWettingen-Ost\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eToll road\\u003c/div\\u003e\",\n" +
                "                     \"maneuver\" : \"fork-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"sat`Hqfpq@I[CYEu@MeBSqCc@gFSsCUmDMmBIeBAO?AIyAQyCAOGy@?KCa@CYIuAM_CgEky@IcDGeD@aDDmBNiBR_BVwAX_B^cB\\\\oAPk@Ni@LYL[@E`@aA`@y@b@w@d@w@`@m@b@k@h@o@XYXSNMXUz@e@z@c@dAi@fAi@RCbAo@xAaAd@a@f@c@b@e@PSn@aAXi@n@yAHWNc@Lg@No@TgAHi@BUNgAF{@D}@@C@g@?I@Y@m@?iAEuAGiAO{AU}AYwAa@{ASw@_@{A]uAGYUkAQgAOcAQsAOcBK}AC_@C_@?A?C?AAWEgAC{AAeCBoB@S@gBDqDBoB?eCEaCC{AG{BCiAEeBCiB?uA?_A@gAD{AFwAD}@L{ALmAP{ARwAPeAFa@DSFSPs@~@gDn@cBRe@f@eAl@gAj@_A^g@b@g@n@s@p@s@dA_A|@s@fBoAbAq@d@[bAq@~AeAx@i@|@k@ZSr@e@v@i@`BeARMTQz@m@z@k@`@WrAy@lD_CpBsAtA}@dImF|CsBnA}@dBmAnAaAJIh@c@h@e@FGVUVSNO`@a@r@u@X[Z_@VYZa@\\\\c@j@u@f@u@Zg@bAaBHQJQTa@JSFMFINYh@iAf@kAf@kAd@mAPe@Ri@b@oAl@mBf@_B`@qAXaANg@Ne@h@kBp@cCrAqEJ[J[z@kCb@mAz@{Bl@{AvAeDr@_BfAcCfAeCjAwCXq@~AmEZaA`@sAd@_B\\\\sAx@{Cn@iCl@_CtAsFZqA`@cBp@mCj@aCv@{Cn@kCtAqFx@iDh@sBH]Lc@\\\\yAfAkEj@{B^{Ad@mB\\\\sAPu@J_@fAoEfA_FlBkJhAkGb@cDl@qEd@mEZ_ELwAN}BJkBHyAF_BJcCNqC@g@@i@Bi@@mA@y@@[F_CFmCDkB?E?E@[?GD}@BoBF_BFiAF}CDaANmDN{CL}BJ{AJyAB_@@E@Yd@_FBW@INkALgAZqC\\\\aC\\\\kCZuBXeB^wBZaBLm@@GHc@tAsGdAmEvB_I`EgM@A@EHUnDeJ|BeG~A_EFOlB}E|@_CN_@l@aB`AmCzAgE\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.4577013,\n" +
                "                        \"lng\" : 8.280250799999999\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"6.0 km\",\n" +
                "                        \"value\" : 6042\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"4 mins\",\n" +
                "                        \"value\" : 227\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.39335670000001,\n" +
                "                        \"lng\" : 8.4949934\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Keep \\u003cb\\u003eleft\\u003c/b\\u003e to continue on \\u003cb\\u003eA1H\\u003c/b\\u003e, follow signs for \\u003cb\\u003eZÃ¼rich-City\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eToll road\\u003c/div\\u003e\",\n" +
                "                     \"maneuver\" : \"keep-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"gnk`Hcdlr@Nq@H[HUh@_BNa@HYH[z@kC~@eDbB{GRy@Pw@VqA~@aFJs@Lu@\\\\cCt@_GB]JoAHy@Fo@b@gFLgCH_BDuABe@?A@}@FyBBmCBaDA}CCwBA}AEoCEeCEgBEqBE{A?o@CyAA{AA_@?}AAmA@oA@_A?u@BmABaABu@FmAD}@Dq@BWB]Dg@Jy@H{@R}ATyA\\\\gBXsAXiAZgAX_A^iA`@gAb@eA`@y@h@eAd@}@Xg@tAuBd@aAb@y@j@cAn@kA`@}@`@_At@mBb@wA\\\\sAV_AFWDQ`@{BN}@Jy@^eDJaALyAPsBPsBN{ARuBR_BLy@BMTuAZ_BXqAVcA^{A`@oATs@Rk@BGXq@BI^_A`@}@`@}@P[DIHS\\\\m@b@{@f@{@h@_Av@oAdAcBrAsBrAsB|@wAjAkBbAaB~AwCNY?AP]P_@Zs@h@kAp@_BJW@AL]`@kA`@kAn@sBj@oBh@oBf@wBd@mB^iB^eBX}Ab@}B\\\\sB|@kFJk@Jm@j@oDt@qEJi@d@iDBIb@yCt@sFVkCXkCb@sENiCDoAFuBFoD\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.4136384,\n" +
                "                        \"lng\" : 8.423223699999999\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.3 km\",\n" +
                "                        \"value\" : 343\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 39\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.3934177,\n" +
                "                        \"lng\" : 8.499550299999999\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Continue onto \\u003cb\\u003eRoute 1\\u003c/b\\u003e/\\u003cb\\u003eRoute 3\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePartial toll road\\u003c/div\\u003e\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"oog`Hudzr@?a@?]@_BAqBAeCC{CA}AAcB?i@AoB\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.39335670000001,\n" +
                "                        \"lng\" : 8.4949934\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.4 km\",\n" +
                "                        \"value\" : 429\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"3 mins\",\n" +
                "                        \"value\" : 166\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.3939026,\n" +
                "                        \"lng\" : 8.5051837\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Keep \\u003cb\\u003eleft\\u003c/b\\u003e to continue on \\u003cb\\u003eBernerstrasse\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"keep-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"{og`Hea{r@Eg@Ac@A]AYE}AAMAYCe@Em@C_@Ks@EUCICI?AAKAIAy@AUG_DGgECq@?OAmA?GCa@\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.3934177,\n" +
                "                        \"lng\" : 8.499550299999999\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"1.4 km\",\n" +
                "                        \"value\" : 1405\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"5 mins\",\n" +
                "                        \"value\" : 306\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.391096,\n" +
                "                        \"lng\" : 8.5227588\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"\\u003cb\\u003eBernerstrasse\\u003c/b\\u003e turns slightly \\u003cb\\u003eright\\u003c/b\\u003e and becomes \\u003cb\\u003eHardturmstrasse\\u003c/b\\u003e/\\u003cb\\u003eRoute 1\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eContinue to follow Hardturmstrasse\\u003c/div\\u003e\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"{rg`Hkd|r@?UBW@w@@q@BkCDgD@_@ByBDeDB{A@aA@mA?MBeCDoB@g@BU?MDk@Dg@D[Jk@D]bAmFf@kCr@wD|@oETyAj@mCFYPaA\\\\iBBQp@cDl@eCDIP{@PaAPcADKFOHk@BQ?I@K?OAGAGKMIGACIEEGGICKAGCKAI?E?I?E\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.3939026,\n" +
                "                        \"lng\" : 8.5051837\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.2 km\",\n" +
                "                        \"value\" : 223\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 37\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.3903274,\n" +
                "                        \"lng\" : 8.5253005\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eZÃ¶llystrasse\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"kag`Hgr_s@FU@ADIFIDGRKJCDCDE@EFM@KBM?KAO?I?K?M@K?M@I?IDg@?K@M@K@K@G?E@G@GBOBMBOBKBMBI@IBGBSHa@\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.391096,\n" +
                "                        \"lng\" : 8.5227588\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"1.6 km\",\n" +
                "                        \"value\" : 1612\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"3 mins\",\n" +
                "                        \"value\" : 193\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.3796133,\n" +
                "                        \"lng\" : 8.5375502\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Continue onto \\u003cb\\u003eSihlquai\\u003c/b\\u003e\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"q|f`Hcb`s@FONa@hAiCJSFMDMFOLUl@sALYN_@FMBKDMDG@EBGDGR[T[bAyAVa@^i@^k@LQLSHMLQBGFIDGzB_Eh@gAxAsCRa@t@yAJUN]j@mA^w@bBkDrAoCZk@nDmHJQNWNUR[RUVYVUVUb@_@XOPMPKBARKXK`@OTGTEZG^GVANCTAJ?J@t@Lb@JdAd@fAh@TFLB\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.3903274,\n" +
                "                        \"lng\" : 8.5253005\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.3 km\",\n" +
                "                        \"value\" : 340\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"2 mins\",\n" +
                "                        \"value\" : 102\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.3782872,\n" +
                "                        \"lng\" : 8.5413707\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"\\u003cb\\u003eSihlquai\\u003c/b\\u003e turns \\u003cb\\u003eleft\\u003c/b\\u003e and becomes \\u003cb\\u003eMuseumstrasse\\u003c/b\\u003e\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"qyd`Hunbs@@B@@@@@@BBB@B@@A@?@A@ADGXiAf@aBZyA`@}BBOHe@PaANs@D[BM@MD]?CDWBYLg@J[\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.3796133,\n" +
                "                        \"lng\" : 8.5375502\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"32 m\",\n" +
                "                        \"value\" : 32\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 12\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.3780099,\n" +
                "                        \"lng\" : 8.541505000000001\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eBahnhofquai\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"iqd`Hqfcs@j@SJG\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.3782872,\n" +
                "                        \"lng\" : 8.5413707\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.1 km\",\n" +
                "                        \"value\" : 108\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 35\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.377122,\n" +
                "                        \"lng\" : 8.5411\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Slight \\u003cb\\u003eright\\u003c/b\\u003e to stay on \\u003cb\\u003eBahnhofquai\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-slight-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"qod`Hmgcs@V?B?L?P?TBD@B?\\\\LXPBDHJHJDL\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.3780099,\n" +
                "                        \"lng\" : 8.541505000000001\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.2 km\",\n" +
                "                        \"value\" : 166\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 47\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.3774715,\n" +
                "                        \"lng\" : 8.539004799999999\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Slight \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eBahnhofpl.\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-slight-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"_jd`H{dcs@@J?T@x@?Z?NANANG^Kf@GXG^I\\\\?DCLEVCFAF?BCFCR\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.377122,\n" +
                "                        \"lng\" : 8.5411\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.3 km\",\n" +
                "                        \"value\" : 264\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"2 mins\",\n" +
                "                        \"value\" : 109\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 47.3768986,\n" +
                "                        \"lng\" : 8.541699600000001\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Make a \\u003cb\\u003eU-turn\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"uturn-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"eld`Hwwbs@?D?B?B@B@B@B\\\\\\\\VsABODSF_@Hc@?CBOFY@E@K?A@QBg@?Q?U?UA]C}@BqABU@Q?G\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 47.3774715,\n" +
                "                        \"lng\" : 8.539004799999999\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"traffic_speed_entry\" : [],\n" +
                "               \"via_waypoint\" : []\n" +
                "            }\n" +
                "         ],\n" +
                "         \"overview_polyline\" : {\n" +
                "            \"points\" : \"yz|zGujng@pBkXgCaBwMsPuA_GmIdAuUkFgRuV{IAQImR{NcW_VuBaLjY{lA~Fyg@bXm_@|x@uY|Vkb@vNe_Apo@}{@nc@il@xOsm@qDugAxLse@hMsu@`Rqm@`c@abBx[qzAl]}bAhHsj@lHiwBm@ugBaNczA~Lyc@EfIgd@ms@iIyNWqT~Re]MoZ}MqMqXoBkUcNua@qPmb@oUkWs[{Wk\\\\sYsRqm@uWwYmAah@|Aqs@aQeyBkGcr@aLuz@wZet@ud@}u@su@{o@}}@c\\\\mv@{g@k}@qJy_@ce@cxEeLy_C}Paw@ob@kt@sfAubAo^is@cb@ma@_hBkh@aj@sOq_@ie@oUgl@yQqIsMb@cj@~Tg`@`Jm`@iSoc@kJe}@dj@{e@aFms@dAyk@u@cc@kOqb@oJaU`Bsl@qGwh@{Foe@xTkZiN__@w}@s|@u`AaL{qAwXer@oZ{X_]dFyV{DiPqXsKy^sm@qg@ea@uYm{@exAgn@qsBkdAo`CsRcdAyEibAwGexBeQqdAiDs_AsO_j@cm@_}@yO}r@uCudAnEknBeLidB{R{]{WyLy`A{c@}q@}\\\\{_@aa@w]wu@s\\\\ka@oO_lAwZg`@{N}c@e|@cFyIa^}a@iq@me@uq@cL}`@kUm_BWqyAqJcm@}Mf@u]hScb@\\\\kVyYoLmh@cd@}j@uYuZew@_k@_[hAmZuMus@uoCgJaQoWsQgk@oE_n@kOsj@ut@qRgh@s]_fAe{@wt@idAok@qe@lAscAwGyuCci@qhB_m@ko@{Pc`@Bay@vNsaAaEqvBoz@oyB{H}[cMu[eZqm@mfBkq@guA{H}m@_i@o|@{a@q`BiUak@oHeu@qEq`AeRet@w]qf@ogAud@oh@}e@{Y}q@q`BucGem@iwCgF{oA`Cay@nK}mAeA_y@oLcbAnA}}@_Ha{CnPoqAz_@ujAfBkm@uJ_n@iPc[{\\\\ie@gOke@w\\\\goBgNexBrDmw@oDix@nI{v@q@ko@_K}[cmAgiBytAkxAcmAioBkb@s~BoYms@uy@w`Aac@ui@uTclA|BsrA}KaeBiOqhBmQsmCaTei@qZ__@ib@ao@we@y[}d@qp@ka@aa@klAsb@}Zy\\\\qPeh@g]{yAgRao@_G_y@yGs_BbKa^tUmPhGcVyHq_ALgq@vLu_@tkAsy@r[gl@pn@ipBdTc|@jLigAtHyrAzp@qxBbN}{@?es@\\\\s_@bKya@~Peb@fHgj@fd@_}@lOuo@dKs`AeBmz@fIenA`Fwa@`P{c@tc@ew@|QXpEcU|F[DpJj@{M\"\n" +
                "         },\n" +
                "         \"summary\" : \"A12 and A1\",\n" +
                "         \"warnings\" : [],\n" +
                "         \"waypoint_order\" : []\n" +
                "      }\n" +
                "   ],\n" +
                "   \"status\" : \"OK\"\n" +
                "}";
    }
}
