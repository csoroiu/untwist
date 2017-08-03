package ro.derbederos.untwist;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static ro.derbederos.untwist.ArrayUtils.generateLongArray;
import static ro.derbederos.untwist.Utils.reverseArray;

public class ReversibleMersenneTwisterTest extends ReverseRandomGeneratorAbstractTest<ReversibleMersenneTwister> {

    private static final int COMPARE_STEPS = 10_000;
    private static final int[] ARRAY_SEED = {0x123, 0x234, 0x345, 0x456};
    private static final int INT_SEED = 1234557890;

    public ReversibleMersenneTwisterTest() {
    }

    protected ReversibleMersenneTwister makeGenerator() {
        return new ReversibleMersenneTwister(ARRAY_SEED);
    }

    int[] getMakotoNishimuraTestSeed() {
        return ARRAY_SEED;
    }

    @Test
    public void testMakotoNishimura() {
        ReversibleMersenneTwister mt = makeGenerator();
        mt.setSeed(getMakotoNishimuraTestSeed());
        long[] refInt = {
                1067595299L, 955945823L, 477289528L, 4107218783L, 4228976476L, 3344332714L, 3355579695L, 227628506L,
                810200273L, 2591290167L, 2560260675L, 3242736208L, 646746669L, 1479517882L, 4245472273L, 1143372638L,
                3863670494L, 3221021970L, 1773610557L, 1138697238L, 1421897700L, 1269916527L, 2859934041L, 1764463362L,
                3874892047L, 3965319921L, 72549643L, 2383988930L, 2600218693L, 3237492380L, 2792901476L, 725331109L,
                605841842L, 271258942L, 715137098L, 3297999536L, 1322965544L, 4229579109L, 1395091102L, 3735697720L,
                2101727825L, 3730287744L, 2950434330L, 1661921839L, 2895579582L, 2370511479L, 1004092106L, 2247096681L,
                2111242379L, 3237345263L, 4082424759L, 219785033L, 2454039889L, 3709582971L, 835606218L, 2411949883L,
                2735205030L, 756421180L, 2175209704L, 1873865952L, 2762534237L, 4161807854L, 3351099340L, 181129879L,
                3269891896L, 776029799L, 2218161979L, 3001745796L, 1866825872L, 2133627728L, 34862734L, 1191934573L,
                3102311354L, 2916517763L, 1012402762L, 2184831317L, 4257399449L, 2899497138L, 3818095062L, 3030756734L,
                1282161629L, 420003642L, 2326421477L, 2741455717L, 1278020671L, 3744179621L, 271777016L, 2626330018L,
                2560563991L, 3055977700L, 4233527566L, 1228397661L, 3595579322L, 1077915006L, 2395931898L, 1851927286L,
                3013683506L, 1999971931L, 3006888962L, 1049781534L, 1488758959L, 3491776230L, 104418065L, 2448267297L,
                3075614115L, 3872332600L, 891912190L, 3936547759L, 2269180963L, 2633455084L, 1047636807L, 2604612377L,
                2709305729L, 1952216715L, 207593580L, 2849898034L, 670771757L, 2210471108L, 467711165L, 263046873L,
                3569667915L, 1042291111L, 3863517079L, 1464270005L, 2758321352L, 3790799816L, 2301278724L, 3106281430L,
                7974801L, 2792461636L, 555991332L, 621766759L, 1322453093L, 853629228L, 686962251L, 1455120532L,
                957753161L, 1802033300L, 1021534190L, 3486047311L, 1902128914L, 3701138056L, 4176424663L, 1795608698L,
                560858864L, 3737752754L, 3141170998L, 1553553385L, 3367807274L, 711546358L, 2475125503L, 262969859L,
                251416325L, 2980076994L, 1806565895L, 969527843L, 3529327173L, 2736343040L, 2987196734L, 1649016367L,
                2206175811L, 3048174801L, 3662503553L, 3138851612L, 2660143804L, 1663017612L, 1816683231L, 411916003L,
                3887461314L, 2347044079L, 1015311755L, 1203592432L, 2170947766L, 2569420716L, 813872093L, 1105387678L,
                1431142475L, 220570551L, 4243632715L, 4179591855L, 2607469131L, 3090613241L, 282341803L, 1734241730L,
                1391822177L, 1001254810L, 827927915L, 1886687171L, 3935097347L, 2631788714L, 3905163266L, 110554195L,
                2447955646L, 3717202975L, 3304793075L, 3739614479L, 3059127468L, 953919171L, 2590123714L, 1132511021L,
                3795593679L, 2788030429L, 982155079L, 3472349556L, 859942552L, 2681007391L, 2299624053L, 647443547L,
                233600422L, 608168955L, 3689327453L, 1849778220L, 1608438222L, 3968158357L, 2692977776L, 2851872572L,
                246750393L, 3582818628L, 3329652309L, 4036366910L, 1012970930L, 950780808L, 3959768744L, 2538550045L,
                191422718L, 2658142375L, 3276369011L, 2927737484L, 1234200027L, 1920815603L, 3536074689L, 1535612501L,
                2184142071L, 3276955054L, 428488088L, 2378411984L, 4059769550L, 3913744741L, 2732139246L, 64369859L,
                3755670074L, 842839565L, 2819894466L, 2414718973L, 1010060670L, 1839715346L, 2410311136L, 152774329L,
                3485009480L, 4102101512L, 2852724304L, 879944024L, 1785007662L, 2748284463L, 1354768064L, 3267784736L,
                2269127717L, 3001240761L, 3179796763L, 895723219L, 865924942L, 4291570937L, 89355264L, 1471026971L,
                4114180745L, 3201939751L, 2867476999L, 2460866060L, 3603874571L, 2238880432L, 3308416168L, 2072246611L,
                2755653839L, 3773737248L, 1709066580L, 4282731467L, 2746170170L, 2832568330L, 433439009L, 3175778732L,
                26248366L, 2551382801L, 183214346L, 3893339516L, 1928168445L, 1337157619L, 3429096554L, 3275170900L,
                1782047316L, 4264403756L, 1876594403L, 4289659572L, 3223834894L, 1728705513L, 4068244734L, 2867840287L,
                1147798696L, 302879820L, 1730407747L, 1923824407L, 1180597908L, 1569786639L, 198796327L, 560793173L,
                2107345620L, 2705990316L, 3448772106L, 3678374155L, 758635715L, 884524671L, 486356516L, 1774865603L,
                3881226226L, 2635213607L, 1181121587L, 1508809820L, 3178988241L, 1594193633L, 1235154121L, 326117244L,
                2304031425L, 937054774L, 2687415945L, 3192389340L, 2003740439L, 1823766188L, 2759543402L, 10067710L,
                1533252662L, 4132494984L, 82378136L, 420615890L, 3467563163L, 541562091L, 3535949864L, 2277319197L,
                3330822853L, 3215654174L, 4113831979L, 4204996991L, 2162248333L, 3255093522L, 2219088909L, 2978279037L,
                255818579L, 2859348628L, 3097280311L, 2569721123L, 1861951120L, 2907080079L, 2719467166L, 998319094L,
                2521935127L, 2404125338L, 259456032L, 2086860995L, 1839848496L, 1893547357L, 2527997525L, 1489393124L,
                2860855349L, 76448234L, 2264934035L, 744914583L, 2586791259L, 1385380501L, 66529922L, 1819103258L,
                1899300332L, 2098173828L, 1793831094L, 276463159L, 360132945L, 4178212058L, 595015228L, 177071838L,
                2800080290L, 1573557746L, 1548998935L, 378454223L, 1460534296L, 1116274283L, 3112385063L, 3709761796L,
                827999348L, 3580042847L, 1913901014L, 614021289L, 4278528023L, 1905177404L, 45407939L, 3298183234L,
                1184848810L, 3644926330L, 3923635459L, 1627046213L, 3677876759L, 969772772L, 1160524753L, 1522441192L,
                452369933L, 1527502551L, 832490847L, 1003299676L, 1071381111L, 2891255476L, 973747308L, 4086897108L,
                1847554542L, 3895651598L, 2227820339L, 1621250941L, 2881344691L, 3583565821L, 3510404498L, 849362119L,
                862871471L, 797858058L, 2867774932L, 2821282612L, 3272403146L, 3997979905L, 209178708L, 1805135652L,
                6783381L, 2823361423L, 792580494L, 4263749770L, 776439581L, 3798193823L, 2853444094L, 2729507474L,
                1071873341L, 1329010206L, 1289336450L, 3327680758L, 2011491779L, 80157208L, 922428856L, 1158943220L,
                1667230961L, 2461022820L, 2608845159L, 387516115L, 3345351910L, 1495629111L, 4098154157L, 3156649613L,
                3525698599L, 4134908037L, 446713264L, 2137537399L, 3617403512L, 813966752L, 1157943946L, 3734692965L,
                1680301658L, 3180398473L, 3509854711L, 2228114612L, 1008102291L, 486805123L, 863791847L, 3189125290L,
                1050308116L, 3777341526L, 4291726501L, 844061465L, 1347461791L, 2826481581L, 745465012L, 2055805750L,
                4260209475L, 2386693097L, 2980646741L, 447229436L, 2077782664L, 1232942813L, 4023002732L, 1399011509L,
                3140569849L, 2579909222L, 3794857471L, 900758066L, 2887199683L, 1720257997L, 3367494931L, 2668921229L,
                955539029L, 3818726432L, 1105704962L, 3889207255L, 2277369307L, 2746484505L, 1761846513L, 2413916784L,
                2685127085L, 4240257943L, 1166726899L, 4215215715L, 3082092067L, 3960461946L, 1663304043L, 2087473241L,
                4162589986L, 2507310778L, 1579665506L, 767234210L, 970676017L, 492207530L, 1441679602L, 1314785090L,
                3262202570L, 3417091742L, 1561989210L, 3011406780L, 1146609202L, 3262321040L, 1374872171L, 1634688712L,
                1280458888L, 2230023982L, 419323804L, 3262899800L, 39783310L, 1641619040L, 1700368658L, 2207946628L,
                2571300939L, 2424079766L, 780290914L, 2715195096L, 3390957695L, 163151474L, 2309534542L, 1860018424L,
                555755123L, 280320104L, 1604831083L, 2713022383L, 1728987441L, 3639955502L, 623065489L, 3828630947L,
                4275479050L, 3516347383L, 2343951195L, 2430677756L, 635534992L, 3868699749L, 808442435L, 3070644069L,
                4282166003L, 2093181383L, 2023555632L, 1568662086L, 3422372620L, 4134522350L, 3016979543L, 3259320234L,
                2888030729L, 3185253876L, 4258779643L, 1267304371L, 1022517473L, 815943045L, 929020012L, 2995251018L,
                3371283296L, 3608029049L, 2018485115L, 122123397L, 2810669150L, 1411365618L, 1238391329L, 1186786476L,
                3155969091L, 2242941310L, 1765554882L, 279121160L, 4279838515L, 1641578514L, 3796324015L, 13351065L,
                103516986L, 1609694427L, 551411743L, 2493771609L, 1316337047L, 3932650856L, 4189700203L, 463397996L,
                2937735066L, 1855616529L, 2626847990L, 55091862L, 3823351211L, 753448970L, 4045045500L, 1274127772L,
                1124182256L, 92039808L, 2126345552L, 425973257L, 386287896L, 2589870191L, 1987762798L, 4084826973L,
                2172456685L, 3366583455L, 3602966653L, 2378803535L, 2901764433L, 3716929006L, 3710159000L, 2653449155L,
                3469742630L, 3096444476L, 3932564653L, 2595257433L, 318974657L, 3146202484L, 853571438L, 144400272L,
                3768408841L, 782634401L, 2161109003L, 570039522L, 1886241521L, 14249488L, 2230804228L, 1604941699L,
                3928713335L, 3921942509L, 2155806892L, 134366254L, 430507376L, 1924011722L, 276713377L, 196481886L,
                3614810992L, 1610021185L, 1785757066L, 851346168L, 3761148643L, 2918835642L, 3364422385L, 3012284466L,
                3735958851L, 2643153892L, 3778608231L, 1164289832L, 205853021L, 2876112231L, 3503398282L, 3078397001L,
                3472037921L, 1748894853L, 2740861475L, 316056182L, 1660426908L, 168885906L, 956005527L, 3984354789L,
                566521563L, 1001109523L, 1216710575L, 2952284757L, 3834433081L, 3842608301L, 2467352408L, 3974441264L,
                3256601745L, 1409353924L, 1329904859L, 2307560293L, 3125217879L, 3622920184L, 3832785684L, 3882365951L,
                2308537115L, 2659155028L, 1450441945L, 3532257603L, 3186324194L, 1225603425L, 1124246549L, 175808705L,
                3009142319L, 2796710159L, 3651990107L, 160762750L, 1902254979L, 1698648476L, 1134980669L, 497144426L,
                3302689335L, 4057485630L, 3603530763L, 4087252587L, 427812652L, 286876201L, 823134128L, 1627554964L,
                3745564327L, 2589226092L, 4202024494L, 62878473L, 3275585894L, 3987124064L, 2791777159L, 1916869511L,
                2585861905L, 1375038919L, 1403421920L, 60249114L, 3811870450L, 3021498009L, 2612993202L, 528933105L,
                2757361321L, 3341402964L, 2621861700L, 273128190L, 4015252178L, 3094781002L, 1621621288L, 2337611177L,
                1796718448L, 1258965619L, 4241913140L, 2138560392L, 3022190223L, 4174180924L, 450094611L, 3274724580L,
                617150026L, 2704660665L, 1469700689L, 1341616587L, 356715071L, 1188789960L, 2278869135L, 1766569160L,
                2795896635L, 57824704L, 2893496380L, 1235723989L, 1630694347L, 3927960522L, 428891364L, 1814070806L,
                2287999787L, 4125941184L, 3968103889L, 3548724050L, 1025597707L, 1404281500L, 2002212197L, 92429143L,
                2313943944L, 2403086080L, 3006180634L, 3561981764L, 1671860914L, 1768520622L, 1803542985L, 844848113L,
                3006139921L, 1410888995L, 1157749833L, 2125704913L, 1789979528L, 1799263423L, 741157179L, 2405862309L,
                767040434L, 2655241390L, 3663420179L, 2172009096L, 2511931187L, 1680542666L, 231857466L, 1154981000L,
                157168255L, 1454112128L, 3505872099L, 1929775046L, 2309422350L, 2143329496L, 2960716902L, 407610648L,
                2938108129L, 2581749599L, 538837155L, 2342628867L, 430543915L, 740188568L, 1937713272L, 3315215132L,
                2085587024L, 4030765687L, 766054429L, 3517641839L, 689721775L, 1294158986L, 1753287754L, 4202601348L,
                1974852792L, 33459103L, 3568087535L, 3144677435L, 1686130825L, 4134943013L, 3005738435L, 3599293386L,
                426570142L, 754104406L, 3660892564L, 1964545167L, 829466833L, 821587464L, 1746693036L, 1006492428L,
                1595312919L, 1256599985L, 1024482560L, 1897312280L, 2902903201L, 691790057L, 1037515867L, 3176831208L,
                1968401055L, 2173506824L, 1089055278L, 1748401123L, 2941380082L, 968412354L, 1818753861L, 2973200866L,
                3875951774L, 1119354008L, 3988604139L, 1647155589L, 2232450826L, 3486058011L, 3655784043L, 3759258462L,
                847163678L, 1082052057L, 989516446L, 2871541755L, 3196311070L, 3929963078L, 658187585L, 3664944641L,
                2175149170L, 2203709147L, 2756014689L, 2456473919L, 3890267390L, 1293787864L, 2830347984L, 3059280931L,
                4158802520L, 1561677400L, 2586570938L, 783570352L, 1355506163L, 31495586L, 3789437343L, 3340549429L,
                2092501630L, 896419368L, 671715824L, 3530450081L, 3603554138L, 1055991716L, 3442308219L, 1499434728L,
                3130288473L, 3639507000L, 17769680L, 2259741420L, 487032199L, 4227143402L, 3693771256L, 1880482820L,
                3924810796L, 381462353L, 4017855991L, 2452034943L, 2736680833L, 2209866385L, 2128986379L, 437874044L,
                595759426L, 641721026L, 1636065708L, 3899136933L, 629879088L, 3591174506L, 351984326L, 2638783544L,
                2348444281L, 2341604660L, 2123933692L, 143443325L, 1525942256L, 364660499L, 599149312L, 939093251L,
                1523003209L, 106601097L, 376589484L, 1346282236L, 1297387043L, 764598052L, 3741218111L, 933457002L,
                1886424424L, 3219631016L, 525405256L, 3014235619L, 323149677L, 2038881721L, 4100129043L, 2851715101L,
                2984028078L, 1888574695L, 2014194741L, 3515193880L, 4180573530L, 3461824363L, 2641995497L, 3179230245L,
                2902294983L, 2217320456L, 4040852155L, 1784656905L, 3311906931L, 87498458L, 2752971818L, 2635474297L,
                2831215366L, 3682231106L, 2920043893L, 3772929704L, 2816374944L, 309949752L, 2383758854L, 154870719L,
                385111597L, 1191604312L, 1840700563L, 872191186L, 2925548701L, 1310412747L, 2102066999L, 1504727249L,
                3574298750L, 1191230036L, 3330575266L, 3180292097L, 3539347721L, 681369118L, 3305125752L, 3648233597L,
                950049240L, 4173257693L, 1760124957L, 512151405L, 681175196L, 580563018L, 1169662867L, 4015033554L,
                2687781101L, 699691603L, 2673494188L, 1137221356L, 123599888L, 472658308L, 1053598179L, 1012713758L,
                3481064843L, 3759461013L, 3981457956L, 3830587662L, 1877191791L, 3650996736L, 988064871L, 3515461600L,
                4089077232L, 2225147448L, 1249609188L, 2643151863L, 3896204135L, 2416995901L, 1397735321L, 3460025646L
        };
        double[] refDouble = {
                0.76275443, 0.99000644, 0.98670464, 0.10143112, 0.27933125, 0.69867227, 0.94218740, 0.03427201,
                0.78842173, 0.28180608, 0.92179002, 0.20785655, 0.54534773, 0.69644020, 0.38107718, 0.23978165,
                0.65286910, 0.07514568, 0.22765211, 0.94872929, 0.74557914, 0.62664415, 0.54708246, 0.90959343,
                0.42043116, 0.86334511, 0.19189126, 0.14718544, 0.70259889, 0.63426346, 0.77408121, 0.04531601,
                0.04605807, 0.88595519, 0.69398270, 0.05377184, 0.61711170, 0.05565708, 0.10133577, 0.41500776,
                0.91810699, 0.22320679, 0.23353705, 0.92871862, 0.98897234, 0.19786706, 0.80558809, 0.06961067,
                0.55840445, 0.90479405, 0.63288060, 0.95009721, 0.54948447, 0.20645042, 0.45000959, 0.87050869,
                0.70806991, 0.19406895, 0.79286390, 0.49332866, 0.78483914, 0.75145146, 0.12341941, 0.42030252,
                0.16728160, 0.59906494, 0.37575460, 0.97815160, 0.39815952, 0.43595080, 0.04952478, 0.33917805,
                0.76509902, 0.61034321, 0.90654701, 0.92915732, 0.85365931, 0.18812377, 0.65913428, 0.28814566,
                0.59476081, 0.27835931, 0.60722542, 0.68310435, 0.69387186, 0.03699800, 0.65897714, 0.17527003,
                0.02889304, 0.86777366, 0.12352068, 0.91439461, 0.32022990, 0.44445731, 0.34903686, 0.74639273,
                0.65918367, 0.92492794, 0.31872642, 0.77749724, 0.85413832, 0.76385624, 0.32744211, 0.91326300,
                0.27458185, 0.22190155, 0.19865383, 0.31227402, 0.85321225, 0.84243342, 0.78544200, 0.71854080,
                0.92503892, 0.82703064, 0.88306297, 0.47284073, 0.70059042, 0.48003761, 0.38671694, 0.60465770,
                0.41747204, 0.47163243, 0.72750808, 0.65830223, 0.10955369, 0.64215401, 0.23456345, 0.95944940,
                0.72822249, 0.40888451, 0.69980355, 0.26677428, 0.57333635, 0.39791582, 0.85377858, 0.76962816,
                0.72004885, 0.90903087, 0.51376506, 0.37732665, 0.12691640, 0.71249738, 0.81217908, 0.37037313,
                0.32772374, 0.14238259, 0.05614811, 0.74363008, 0.39773267, 0.94859135, 0.31452454, 0.11730313,
                0.62962618, 0.33334237, 0.45547255, 0.10089665, 0.56550662, 0.60539371, 0.16027624, 0.13245301,
                0.60959939, 0.04671662, 0.99356286, 0.57660859, 0.40269560, 0.45274629, 0.06699735, 0.85064246,
                0.87742744, 0.54508392, 0.87242982, 0.29321385, 0.67660627, 0.68230715, 0.79052073, 0.48592054,
                0.25186266, 0.93769755, 0.28565487, 0.47219067, 0.99054882, 0.13155240, 0.47110470, 0.98556600,
                0.84397623, 0.12875246, 0.90953202, 0.49129015, 0.23792727, 0.79481194, 0.44337770, 0.96564297,
                0.67749118, 0.55684872, 0.27286897, 0.79538393, 0.61965356, 0.22487929, 0.02226018, 0.49248200,
                0.42247006, 0.91797788, 0.99250134, 0.23449967, 0.52531508, 0.10246337, 0.78685622, 0.34310922,
                0.89892996, 0.40454552, 0.68608407, 0.30752487, 0.83601319, 0.54956031, 0.63777550, 0.82199797,
                0.24890696, 0.48801123, 0.48661910, 0.51223987, 0.32969635, 0.31075073, 0.21393155, 0.73453207,
                0.15565705, 0.58584522, 0.28976728, 0.97621478, 0.61498701, 0.23891470, 0.28518540, 0.46809591,
                0.18371914, 0.37597910, 0.13492176, 0.66849449, 0.82811466, 0.56240330, 0.37548956, 0.27562998,
                0.27521910, 0.74096121, 0.77176757, 0.13748143, 0.99747138, 0.92504502, 0.09175241, 0.21389176,
                0.21766512, 0.31183245, 0.23271221, 0.21207367, 0.57903312, 0.77523344, 0.13242613, 0.31037988,
                0.01204835, 0.71652949, 0.84487594, 0.14982178, 0.57423142, 0.45677888, 0.48420169, 0.53465428,
                0.52667473, 0.46880526, 0.49849733, 0.05670710, 0.79022476, 0.03872047, 0.21697212, 0.20443086,
                0.28949326, 0.81678186, 0.87629474, 0.92297064, 0.27373097, 0.84625273, 0.51505586, 0.00582792,
                0.33295971, 0.91848412, 0.92537226, 0.91760033, 0.07541125, 0.71745848, 0.61158698, 0.00941650,
                0.03135554, 0.71527471, 0.24821915, 0.63636652, 0.86159918, 0.26450229, 0.60160194, 0.35557725,
                0.24477500, 0.07186456, 0.51757096, 0.62120362, 0.97981062, 0.69954667, 0.21065616, 0.13382753,
                0.27693186, 0.59644095, 0.71500764, 0.04110751, 0.95730081, 0.91600724, 0.47704678, 0.26183479,
                0.34706971, 0.07545431, 0.29398385, 0.93236070, 0.60486023, 0.48015011, 0.08870451, 0.45548581,
                0.91872718, 0.38142712, 0.10668643, 0.01397541, 0.04520355, 0.93822273, 0.18011940, 0.57577277,
                0.91427606, 0.30911399, 0.95853475, 0.23611214, 0.69619891, 0.69601980, 0.76765372, 0.58515930,
                0.49479057, 0.11288752, 0.97187699, 0.32095365, 0.57563608, 0.40760618, 0.78703383, 0.43261152,
                0.90877651, 0.84686346, 0.10599030, 0.72872803, 0.19315490, 0.66152912, 0.10210518, 0.06257876,
                0.47950688, 0.47062066, 0.72701157, 0.48915116, 0.66110261, 0.60170685, 0.24516994, 0.12726050,
                0.03451185, 0.90864994, 0.83494878, 0.94800035, 0.91035206, 0.14480751, 0.88458997, 0.53498312,
                0.15963215, 0.55378627, 0.35171349, 0.28719791, 0.09097957, 0.00667896, 0.32309622, 0.87561479,
                0.42534520, 0.91748977, 0.73908457, 0.41793223, 0.99279792, 0.87908370, 0.28458072, 0.59132853,
                0.98672190, 0.28547393, 0.09452165, 0.89910674, 0.53681109, 0.37931425, 0.62683489, 0.56609740,
                0.24801549, 0.52948179, 0.98328855, 0.66403523, 0.55523786, 0.75886666, 0.84784685, 0.86829981,
                0.71448906, 0.84670080, 0.43922919, 0.20771016, 0.64157936, 0.25664246, 0.73055695, 0.86395782,
                0.65852932, 0.99061803, 0.40280575, 0.39146298, 0.07291005, 0.97200603, 0.20555729, 0.59616495,
                0.08138254, 0.45796388, 0.33681125, 0.33989127, 0.18717090, 0.53545811, 0.60550838, 0.86520709,
                0.34290701, 0.72743276, 0.73023855, 0.34195926, 0.65019733, 0.02765254, 0.72575740, 0.32709576,
                0.03420866, 0.26061893, 0.56997511, 0.28439072, 0.84422744, 0.77637570, 0.55982168, 0.06720327,
                0.58449067, 0.71657369, 0.15819609, 0.58042821, 0.07947911, 0.40193792, 0.11376012, 0.88762938,
                0.67532159, 0.71223735, 0.27829114, 0.04806073, 0.21144026, 0.58830274, 0.04140071, 0.43215628,
                0.12952729, 0.94668759, 0.87391019, 0.98382450, 0.27750768, 0.90849647, 0.90962737, 0.59269720,
                0.96102026, 0.49544979, 0.32007095, 0.62585546, 0.03119821, 0.85953001, 0.22017528, 0.05834068,
                0.80731217, 0.53799961, 0.74166948, 0.77426600, 0.43938444, 0.54862081, 0.58575513, 0.15886492,
                0.73214332, 0.11649057, 0.77463977, 0.85788827, 0.17061997, 0.66838056, 0.96076133, 0.07949296,
                0.68521946, 0.89986254, 0.05667410, 0.12741385, 0.83470977, 0.63969104, 0.46612929, 0.10200126,
                0.01194925, 0.10476340, 0.90285217, 0.31221221, 0.32980614, 0.46041971, 0.52024973, 0.05425470,
                0.28330912, 0.60426543, 0.00598243, 0.97244013, 0.21135841, 0.78561597, 0.78428734, 0.63422849,
                0.32909934, 0.44771136, 0.27380750, 0.14966697, 0.18156268, 0.65686758, 0.28726350, 0.97074787,
                0.63676171, 0.96649494, 0.24526295, 0.08297372, 0.54257548, 0.03166785, 0.33735355, 0.15946671,
                0.02102971, 0.46228045, 0.11892296, 0.33408336, 0.29875681, 0.29847692, 0.73767569, 0.02080745,
                0.62980060, 0.08082293, 0.22993106, 0.25031439, 0.87787525, 0.45150053, 0.13673441, 0.63407612,
                0.97907688, 0.52241942, 0.50580158, 0.06273902, 0.05270283, 0.77031811, 0.05113352, 0.24393329,
                0.75036441, 0.37436336, 0.22877652, 0.59975358, 0.85707591, 0.88691457, 0.85547165, 0.36641027,
                0.58720133, 0.45462835, 0.09243817, 0.32981586, 0.07820411, 0.25421519, 0.36004706, 0.60092307,
                0.46192412, 0.36758683, 0.98424170, 0.08019934, 0.68594024, 0.45826386, 0.29962317, 0.79365413,
                0.89231296, 0.49478547, 0.87645944, 0.23590734, 0.28106737, 0.75026285, 0.08136314, 0.79582424,
                0.76010628, 0.82792971, 0.27947652, 0.72482861, 0.82191216, 0.46171689, 0.79189752, 0.96043686,
                0.51609668, 0.88995725, 0.28998963, 0.55191845, 0.03934737, 0.83033700, 0.49553013, 0.98009549,
                0.19017594, 0.98347750, 0.33452066, 0.87144372, 0.72106301, 0.71272114, 0.71465963, 0.88361677,
                0.85571283, 0.73782329, 0.20920458, 0.34855153, 0.46766817, 0.02780062, 0.74898344, 0.03680650,
                0.44866557, 0.77426312, 0.91025891, 0.25195236, 0.87319953, 0.63265037, 0.25552148, 0.27422476,
                0.95217406, 0.39281839, 0.66441573, 0.09158900, 0.94515992, 0.07800798, 0.02507888, 0.39901462,
                0.17382573, 0.12141278, 0.85502334, 0.19902911, 0.02160210, 0.44460522, 0.14688742, 0.68020336,
                0.71323733, 0.60922473, 0.95400380, 0.99611159, 0.90897777, 0.41073520, 0.66206647, 0.32064685,
                0.62805003, 0.50677209, 0.52690101, 0.87473387, 0.73918362, 0.39826974, 0.43683919, 0.80459118,
                0.32422684, 0.01958019, 0.95319576, 0.98326137, 0.83931735, 0.69060863, 0.33671416, 0.68062550,
                0.65152380, 0.33392969, 0.03451730, 0.95227244, 0.68200635, 0.85074171, 0.64721009, 0.51234433,
                0.73402047, 0.00969637, 0.93835057, 0.80803854, 0.31485260, 0.20089527, 0.01323282, 0.59933780,
                0.31584602, 0.20209563, 0.33754800, 0.68604181, 0.24443049, 0.19952227, 0.78162632, 0.10336988,
                0.11360736, 0.23536740, 0.23262256, 0.67803776, 0.48749791, 0.74658435, 0.92156640, 0.56706407,
                0.36683221, 0.99157136, 0.23421374, 0.45183767, 0.91609720, 0.85573315, 0.37706276, 0.77042618,
                0.30891908, 0.40709595, 0.06944866, 0.61342849, 0.88817388, 0.58734506, 0.98711323, 0.14744128,
                0.63242656, 0.87704136, 0.68347125, 0.84446569, 0.43265239, 0.25146321, 0.04130111, 0.34259839,
                0.92697368, 0.40878778, 0.56990338, 0.76204273, 0.19820348, 0.66314909, 0.02482844, 0.06669207,
                0.50205581, 0.26084093, 0.65139159, 0.41650223, 0.09733904, 0.56344203, 0.62651696, 0.67332139,
                0.58037374, 0.47258086, 0.21010758, 0.05713135, 0.89390629, 0.10781246, 0.32037450, 0.07628388,
                0.34227964, 0.42190597, 0.58201860, 0.77363549, 0.49595133, 0.86031236, 0.83906769, 0.81098161,
                0.26694195, 0.14215941, 0.88210306, 0.53634237, 0.12090720, 0.82480459, 0.75930318, 0.31847147,
                0.92768077, 0.01037616, 0.56201727, 0.88107122, 0.35925856, 0.85860762, 0.61109408, 0.70408301,
                0.58434977, 0.92192494, 0.62667915, 0.75988365, 0.06858761, 0.36156496, 0.58057195, 0.13636150,
                0.57719713, 0.59340255, 0.63530602, 0.22976282, 0.71915530, 0.41162531, 0.63979565, 0.09931342,
                0.79344045, 0.10893790, 0.84450224, 0.23122236, 0.99485593, 0.73637397, 0.17276368, 0.13357764,
                0.74965804, 0.64991737, 0.61990341, 0.41523170, 0.05878239, 0.05687301, 0.05497131, 0.42868366,
                0.42571090, 0.25810502, 0.89642955, 0.30439758, 0.39310223, 0.11357431, 0.04288255, 0.23397550,
                0.11200634, 0.85621396, 0.89733974, 0.37508865, 0.42077265, 0.68597384, 0.72781399, 0.19296476,
                0.61699087, 0.31667128, 0.67756410, 0.00177323, 0.05725176, 0.79474693, 0.18885238, 0.06724856,
                0.68193156, 0.42202167, 0.22082041, 0.28554673, 0.64995708, 0.87851940, 0.29124547, 0.61009521,
                0.87374537, 0.05743712, 0.69902994, 0.81925115, 0.45653873, 0.37236821, 0.31118709, 0.52734307,
                0.39672836, 0.38185294, 0.30163915, 0.17374510, 0.04913278, 0.90404879, 0.25742801, 0.58266467,
                0.97663209, 0.79823377, 0.36437958, 0.15206043, 0.26529938, 0.22690047, 0.05839021, 0.84721160,
                0.18622435, 0.37809403, 0.55706977, 0.49828704, 0.47659049, 0.24289680, 0.88477595, 0.07807463,
                0.56245739, 0.73490635, 0.21099431, 0.13164942, 0.75840044, 0.66877037, 0.28988183, 0.44046090,
                0.24967434, 0.80048356, 0.26029740, 0.30416821, 0.64151867, 0.52067892, 0.12880774, 0.85465381,
                0.02690525, 0.19149288, 0.49630295, 0.79682619, 0.43566145, 0.00288078, 0.81484193, 0.03763639,
                0.68529083, 0.01339574, 0.38405386, 0.30537067, 0.22994703, 0.44000045, 0.27217985, 0.53831243,
                0.02870435, 0.86282045, 0.61831306, 0.09164956, 0.25609707, 0.07445781, 0.72185784, 0.90058883,
                0.30070608, 0.94476583, 0.56822213, 0.21933909, 0.96772793, 0.80063440, 0.26307906, 0.31183306,
                0.16501252, 0.55436179, 0.68562285, 0.23829083, 0.86511559, 0.57868991, 0.81888344, 0.20126869,
                0.93172350, 0.66028129, 0.21786948, 0.78515828, 0.10262106, 0.35390326, 0.79303876, 0.63427924,
                0.90479631, 0.31024934, 0.60635447, 0.56198079, 0.63573813, 0.91854197, 0.99701497, 0.83085849,
                0.31692291, 0.01925964, 0.97446405, 0.98751283, 0.60944293, 0.13751018, 0.69519957, 0.68956636,
                0.56969015, 0.46440193, 0.88341765, 0.36754434, 0.89223647, 0.39786427, 0.85055280, 0.12749961,
                0.79452122, 0.89449784, 0.14567830, 0.45716830, 0.74822309, 0.28200437, 0.42546044, 0.17464886,
                0.68308746, 0.65496587, 0.52935411, 0.12736159, 0.61523955, 0.81590528, 0.63107864, 0.39786553,
                0.20102294, 0.53292914, 0.75485590, 0.59847044, 0.32861691, 0.12125866, 0.58917183, 0.07638293,
                0.86845380, 0.29192617, 0.03989733, 0.52180460, 0.32503407, 0.64071852, 0.69516575, 0.74254998,
                0.54587026, 0.48713246, 0.32920155, 0.08719954, 0.63497059, 0.54328459, 0.64178757, 0.45583809,
                0.70694291, 0.85212760, 0.86074305, 0.33163422, 0.85739792, 0.59908488, 0.74566046, 0.72157152
        };

        for (int i = 0; i < refInt.length; ++i) {
            int r = mt.nextInt();
            assertEquals(refInt[i], (r & 0x7fffffffL) | ((r < 0) ? 0x80000000L : 0x0L));
        }

        for (int i = 0; i < refDouble.length; ++i) {
            int r = mt.nextInt();
            assertEquals(refDouble[i],
                    ((r & 0x7fffffffL) | ((r < 0) ? 0x80000000L : 0x0L)) / 4294967296.0,
                    1.0e-8);
        }
    }

    @Test
    public void testNextPrevLongRange() {
        long[] expected = generateLongArray(2459, () -> generator.nextLong(0x7ABCDEL));
        long[] actual = generateLongArray(2459, () -> generator.prevLong(0x7ABCDEL));

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateLongArray(2467, () -> generator.nextLong(0x7ABCDEL));
        actual = generateLongArray(2467, () -> generator.prevLong(0x7ABCDEL));

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextLongRange() {
        long[] expected = generateLongArray(2459, () -> generator.prevLong(0x7ABCDEF8FFFFFFFFL));
        long[] actual = generateLongArray(2459, () -> generator.nextLong(0x7ABCDEF8FFFFFFFFL));

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateLongArray(2467, () -> generator.prevLong(0x7ABCDEF8FFFFFFFFL));
        actual = generateLongArray(2467, () -> generator.nextLong(0x7ABCDEF8FFFFFFFFL));

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testStateNextPrev() {
        int[] expected = generator.getState();

        //going forward
        generator.twist();
        //going back to the initial state
        generator.untwist();

        int[] actual = generator.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testStateNextPrevMany() {
        int errors = 0;

        for (int i = 0; i < COMPARE_STEPS; i++, generator.twist()) {
            int[] expected = generator.getState();

            //going forward
            generator.twist();
            //going back to the initial state
            generator.untwist();

            int[] actual = generator.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
                assertThat(errors, lessThan(10));
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStateNextPrevManyIntSeed() {
        ReversibleMersenneTwister generator = new ReversibleMersenneTwister(INT_SEED);
        int errors = 0;

        for (int i = 0; i < COMPARE_STEPS; i++, generator.twist()) {
            int[] expected = generator.getState();

            //going forward
            generator.twist();
            //going back to the initial state
            generator.untwist();

            int[] actual = generator.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
                assertThat(errors, lessThan(10));
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStatePrevNext() {
        int[] expected = generator.getState();

        //going back
        generator.untwist();
        //going forward to the initial state
        generator.twist();

        int[] actual = generator.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testStatePrevNextMany() {
        int errors = 0;

        for (int i = 0; i < COMPARE_STEPS; i++, generator.untwist()) {
            int[] expected = generator.getState();

            //going back
            generator.untwist();
            //going forward to the initial state
            generator.twist();

            int[] actual = generator.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
                assertThat(errors, lessThan(10));
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStatePrevNextManyIntSeed() {
        ReversibleMersenneTwister generator = new ReversibleMersenneTwister(INT_SEED);
        int errors = 0;

        for (int i = 0; i < COMPARE_STEPS; i++, generator.untwist()) {
            int[] expected = generator.getState();

            //going back
            generator.untwist();
            //going forward to the initial state
            generator.twist();

            int[] actual = generator.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
                assertThat(errors, lessThan(10));
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStatePrevNextVsNextPrev() {
        ReversibleMersenneTwister mt1 = makeGenerator();
        mt1.untwist();
        mt1.twist();
        int[] expected = mt1.getState();


        ReversibleMersenneTwister mt2 = makeGenerator();
        mt2.twist();
        mt2.untwist();
        int[] actual = mt2.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }
}
