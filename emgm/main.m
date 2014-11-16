

ChengData = load('/Users/weirenwang/Downloads/emgm/emgm/Cheng1234.txt','-ascii');
ManasviData = load('/Users/weirenwang/Downloads/emgm/emgm/Manasvi1234.txt','-ascii');
WeirenData = load('/Users/weirenwang/Downloads/emgm/emgm/Weiren1234.txt','-ascii');

%features should in row, examples in column
%20 bin features, 15 examples
ChengData = ChengData';
ManasviData = ManasviData';
WeirenData = WeirenData';


ChengcvFolds = crossvalind('Kfold',size(ChengData,2),10);
ManasviFolds = crossvalind('Kfold',size(ManasviData,2),10);
WeirenFolds = crossvalind('Kfold',size(WeirenData,2),10);

%cross validation
num = 3;
ChengResult = zeros(3);
ManasviResult = zeros(3);
WeirenResult = zeros(3);
for i=1:10
    testIdx=(ChengcvFolds==i);
    trainIdx=~testIdx;
    ChengTrainData = ChengData(:,trainIdx);
    ManasviTrainData = ManasviData(:,trainIdx);
    WeirenTrainData = WeirenData(:,trainIdx);
    
    ChengTestData = ChengData(:,testIdx);
    ManasviTestData = ManasviData(:,testIdx);
    WeirenTestData = WeirenData(:,testIdx);
    
    [Chenglabel,ChengModel,Chengllh] = emgm(ChengTrainData,num);
    [Manasvilabel,ManasviModel,Manasvillh] = emgm(ManasviTrainData,num);
    [Weirenlabel,WeirenModel,Weirenllh] = emgm(WeirenTrainData,num);
    
    %testdata from Cheng
    [R,Chengllh] = expectation(ChengTestData, ChengModel);
    [R,Manasvillh] = expectation(ChengTestData, ManasviModel);
    [R,Weirenllh] = expectation(ChengTestData, WeirenModel);
    
    if(Chengllh>=Manasvillh&&Chengllh>=Weirenllh)
        ChengResult(1) = ChengResult(1) + 1;
    elseif (Manasvillh>=Chengllh && Manasvillh>=Weirenllh)
        ManasviResult(1) = ManasviResult(1)+1;
    else
        WeirenResult(1) = WeirenResult(1)+1;
    end
    
    %testdata from Manasvi
    [R,Chengllh] = expectation(ManasviTestData, ChengModel);
    [R,Manasvillh] = expectation(ManasviTestData, ManasviModel);
    [R,Weirenllh] = expectation(ManasviTestData, WeirenModel);
    
    if(Chengllh>=Manasvillh&&Chengllh>=Weirenllh)
        ChengResult(2) = ChengResult(2) + 1;
    elseif (Manasvillh>=Chengllh && Manasvillh>=Weirenllh)
        ManasviResult(2) = ManasviResult(2)+1;
    else
        WeirenResult(2) = WeirenResult(2)+1;
    end
    
    %testdata from Weiren
    [R,Chengllh] = expectation(WeirenTestData, ChengModel);
    [R,Manasvillh] = expectation(WeirenTestData, ManasviModel);
    [R,Weirenllh] = expectation(WeirenTestData, WeirenModel);
    
    if(Chengllh>=Manasvillh&&Chengllh>=Weirenllh)
        ChengResult(3) = ChengResult(3) + 1;
    elseif (Manasvillh>=Chengllh && Manasvillh>=Weirenllh)
        ManasviResult(3) = ManasviResult(3)+1;
    else
        WeirenResult(3) = WeirenResult(3)+1;
    end
end
    if(ChengResult(1)>=ManasviResult(1)&&ChengResult(1)>=WeirenResult(1))
        fprintf('Cheng\n'); 
    elseif (ManasviResult(1)>=ChengResult(1) && ManasviResult(1)>=WeirenResult(1))
        fprintf('Manasvi\n'); 
    else
        fprintf('Weiren\n'); 
    end
    disp(ChengResult(1));
    disp(ManasviResult(1));
    disp(WeirenResult(1));
    
    
    if(ChengResult(2)>=ManasviResult(2)&&ChengResult(2)>=WeirenResult(2))
        fprintf('Cheng\n'); 
    elseif (ManasviResult(2)>=ChengResult(2) && ManasviResult(2)>=WeirenResult(2))
        fprintf('Manasvi\n'); 
    else
        fprintf('Weiren\n'); 
    end
    disp(ChengResult(2));
    disp(ManasviResult(2));
    disp(WeirenResult(2));
    
    
     if(ChengResult(3)>=ManasviResult(3)&&ChengResult(3)>=WeirenResult(3))
        fprintf('Cheng\n'); 
    elseif (ManasviResult(3)>=ChengResult(3) && ManasviResult(3)>=WeirenResult(3))
        fprintf('Manasvi\n'); 
    else
        fprintf('Weiren\n'); 
    end
    disp(ChengResult(3));
    disp(ManasviResult(3));
    disp(WeirenResult(3));




    
    



