package com.syllabus.pq

/*
val gson = Gson()
val questionsData: QuestionsData = gson.fromJson(jsonString, QuestionsData::class.java)
val t: MutableMap.MutableEntry<String, String>

AndroidView(
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = unitId
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )

fun getDb(range: String = values.value.db1.toDecodedString()):String{

        return runBlocking {
            val db = withContext(CoroutineScope(Dispatchers.IO).coroutineContext){
                SRMSDatabase(values.value.dba.fullDecode(), appName).readCell(range = range)
            }
            if(db!=null){
                db
            }else{
                defaultDB
            }
        }
    }

val gson = Gson()
val badges: List<Badge> = gson.fromJson(json, Array<Badge>::class.java).toList()


LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            contentPadding = PaddingValues(5.dp)
        ) {
            items(filteredApps) { appInfo ->
                AppItem(appInfo, packageManager){
                    showState=it
                    onClick(showState)
                }
            }
        }


if(startIntentShare){
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        }

        // Start the sharing activity
        startActivity(LocalContext.current, Intent.createChooser(sendIntent, null),null)
        startIntentShare = !startIntentShare
    }

val gson = Gson()

    // Parse JSON into a map using TypeToken
    val playerType = object : TypeToken<Map<String, Player>>() {}.type
    val players: Map<String, Player> = gson.fromJson(jsonString, playerType)

.border(
                BorderStroke(width = 2.dp, color = selectedColor),
                shape = RoundedCornerShape(6)
            )
            .wrapContentHeight()
            .fillMaxWidth(0.9f)
            .background(color = Color(0xFFFFFFFF).copy(alpha = 1.0f), shape = RoundedCornerShape(6))

@Composable
fun Rankings(){
    val scrollState = rememberScrollState()
    var rankingSize by remember {
        mutableStateOf(0.2f)
    }
    var showDetails by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit){
        if(rankingSize < 0.9f){
            while (rankingSize < 0.9f){
                rankingSize+=0.1f
                delay(20)
            }
            rankingSize+=0.07f
            showDetails = true
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(rankingSize)
            .background(color = Color(0xFF1F0888).copy(alpha = 0.8f), shape = RoundedCornerShape(6))
            .border(
                BorderStroke(width = 2.dp, color = Color(0xFFF7F7F8)),
                shape = RoundedCornerShape(6)
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Button(onClick = { dataProvider.showRankings.value = false}) {
            Text(text ="Close")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){

        }
    }
}
 */