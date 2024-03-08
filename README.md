## 1. clone을 진행할 로컬저장소 만들기
로컬저장소로 쓸 새로 만든 폴더에 마우스를 올리고 마우스 우클릭 -> git bash here을 클릭해서 git bash를 실행합니다.

```
    $git init
    $git clone 새리포지토리주소
```

## 2. Clone 한 리포지토리에 commit 기록 남기기
clone한 리포지토리는 새롭게 생성 후 어떠한 행동(commit, push 등)을 하지 않았으므로 아무런 기록이 없습니다. 
해당 상태에서 여러 리포지토리를 합치는 과정을 진행한다면 Error가 발생합니다. 에러 방지를 위해 복제한 리포지토리에 commit 기록을 남깁니다.

```
    $git add .
    $git commit -m 'commit test'
```

## 3. 합칠 리포지토리들을 새롭게 생성한 리포지토리로 합치기
GitHub에서 브랜치명이 master 인지, main 인지 확인하시고 기존브랜치명을 입력합니다. 
기존브랜치명이 일치 하지 않으면 'fatal: couldn't find remote ref 브랜치명' 에러를 만나게 됩니다. 
```
    $git subtree add --prefix=기존리포지토리명 기존리포지토리주소 기존브랜치명
```
여러개의 리포지토리를 위의 형식과 동일하게 입력합니다. 

## 4. 새로 만든 리포지토리에 Push하기
```
    $ git push origin HEAD:master --force
```
HEAD:main 으로 안하고 HEAD:master 로 했더니 master에 합쳐진 파일들을 볼 수 있었습니다. 

아래와 같이 에러를 만날 수 있습니다. 

```
    $ git push origin HEAD:master --force
    fatal: 'origin' does not appear to be a git repository
    fatal: Could not read from remote repository.

    Please make sure you have the correct access rights
    and the repository exists.
```

이 경우에는 해당 리포지토리를 다시 연결해 줍니다. 
```
    $ git remote add origin 새리포지토리주소
```
다시 Push 합니다. 아래와 같이 메시지가 나오면 정상적으로 Push 된 것입니다. GitHub에 들어가 새로운 리포지토리에 올라간 파일을 확인 할 수 있습니다.
```
    $ git push origin HEAD:master --force
    Enumerating objects: 232, done.
    Counting objects: 100% (232/232), done.
    Delta compression using up to 8 threads
    Compressing objects: 100% (161/161), done.
    Writing objects: 100% (232/232), 5.58 MiB | 4.17 MiB/s, done.
    Total 232 (delta 63), reused 90 (delta 15), pack-reused 0
    remote: Resolving deltas: 100% (63/63), done.
    remote:
    remote: Create a pull request for 'master' on GitHub by visiting:
    remote:      https://github.com/{githubid}/{새스토리지명}/pull/new/master
    remote:
    To https://github.com/{githubid}/{새스토리지명}.git
     * [new branch]      HEAD -> master
```
### 많은 리포지토리는 정리되고 commit기록은 그대로 유지할 수 있습니다. 

