##1.在本地目录下关联远程repository:
  - origin给远程起的别名
  
  git remote add origin git@github.com:git_username/repository_name.git

##2.取消本地目录下关联的远程库：
    git remote remove origin

##3.fatal: refusing to merge unrelated histories：
解决方案：git pull origin master --allow-unrelated-histories

##4.mac os下输入提交信息操作：
  1. press "i"
  2. write your merge message
  3. press "esc"
  4. write ":wq"
  5. then press enter

##5.查看本地分支
  git branch
##6.查看本地,远程分支
  git branch -a
##7.查看关联的远程仓库的信息
  git remote -v

##8.git branch branchName:
  创建本地分支，但是不会自动切换到这个分支

##9.git checkout -b branchName:
  创建本地分支，并且立即切换到新分支

##10.git merge branchName:
   将名称为branchName的分支合并到当前分支

##11.git push origin localBranch:remoteBranch
   提交本地分支localBranch,作为远程remoteBranch的分支，git会在远程创建名称为remoteBranch的分支 

##12.git reset --hard
   还原到上次提交
   然后执行git push -f origin HEAD:remoteBranchName将该次提交置顶，同步

##13.标签
   git tag -a v1.4 -m 'my version 1.4’打标签
   git push origin [tagname] 推送标签到服务器上

##14.查看远程分支
   git branch -r

##15.拉取远程分支到本地
   git checkout -b 本地分支名x origin/远程分支名x
   会在本地新建分支x，并自动切换到该本地分支x
   git fetch origin 远程分支名x:本地分支名x
   会在本地新建分支x，但是不会自动切换到该本地分支x，需要手动checkout。

##16.清空工作进度
   git stash clear

##17.master分支合并develop分支，不使用fast-farward merge
  # 切换到Master分支
　　git checkout master

　　# 对Develop分支进行合并
　　git merge --no-ff develop
   会在master分支新建一个节点，而git merge develop直接Git执行"快进式合并"（fast-farward merge）
   会直接将Master分支指向develop分支

##18.本地分支与远程分支关联
  git branch --set-upstream-to origin/branchName

##19.本地分支重命名
   git branch -m oldName newName

##20.拉取远程分支到本地并在本地创建分支
   1. git fetch origin remoteBranchname:localBranchname
   可以把远程某各分支拉去到本地的localBranchname下，如果没有localBranchname，则会在本地新建localBranchname

   2. git checkout -b newBrach origin/master
   
   获取远程分支remoteName 到本地新分支localName，并跳到localName分支

##21.本地提交
   1. git add.
   2. git commit -m “提交信息”


adb push ./app/build/outputs/patch/ViaBTC/release/patch_signed_7zip.apk /storage/sdcard0/


  


