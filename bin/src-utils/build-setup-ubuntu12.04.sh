#!/usr/bin/env bash
#Requires root to run the script
#Copyright (c) 2016 Zenko Klapko Jr.

arch=`uname -m`

if [ $arch == "i386" ] || [ $arch == "i586" ] || [ $arch == "i686" ] ; then
  ARCH=32
elif [ $arch == "x86_64" ] ; then
  ARCH=64
else
  echo "Unknown processor architecture: $arch"
  exit 1
fi

# Precise needs to install python-software-properties to use add-apt-repository
apt-get -y update
apt-get -y install python-software-properties

# add keys for ubuntu-toolchain-r ppa so that we can install gcc-5.x instead of compiling it
add-apt-repository -y ppa:ubuntu-toolchain-r/test

# intall keys for ceph release
wget -q -O- https://raw.github.com/ceph/ceph/master/keys/autobuild.asc \ | sudo apt-key add -

# add the ceph repo 
echo deb http://gitbuilder.ceph.com/ceph-deb-$(lsb_release -sc)-$arch-basic/ref/master $(lsb_release -sc) main | sudo tee /etc/apt/sources.list.d/ceph.list


apt-get -y update
#Temporarily removing libboost from package manager and will compile
apt-get -y --allow-unauthenticated install zip gcc-5 g++-5 cmake liblog4cpp5-dev libbz2-dev git-core cronolog zlib1g-dev libexpat1-dev libncurses5-dev libreadline6-dev rrdtool librrd-dev libart-2.0-2 libart-2.0-dev
#apt-get -y --allow-unauthenticated install zip g++ cmake liblog4cpp5-dev libbz2-dev git-core cronolog zlib1g-dev libexpat1-dev libncurses5-dev libreadline6-dev rrdtool librrd-dev libart-2.0-2 libart-2.0-dev libboost1.55-all-dev

# no install ceph and the new lib and dev headers - posibly dont need ceph just to build but is needed for testing
apt-get -y install ceph libcephfs1 libcephfs-dev

#Need to update alternatives to use the new compiler
update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-5 10
update-alternatives --install /usr/bin/g++ g++ /usr/bin/g++-5 10

apt-get -y --allow-unauthenticated install openjdk-6-jdk maven


apt-get -y --allow-unauthenticated install ant autoconf automake libtool bison flex pkg-config php5 php5-dev php5-cli ruby ruby-dev python-dev libhttp-access2-ruby libbit-vector-perl libclass-accessor-chained-perl libssh-dev nodejs

# Going to comment out and use the test ppa for gcc-5.x
# Need the following prerequisties to build gcc52
#cd ~
#wget http://mirrors.ibiblio.org/gnu/ftp/gnu/gmp/gmp-6.1.0.tar.bz2
#tar -jxf gmp-6.1.0.tar.bz2
#cd gmp-6.1.0/
#./configure
#make
#make install
#cd ~
#wget http://mirrors.ibiblio.org/gnu/ftp/gnu/mpfr/mpfr-3.1.3.tar.bz2
#tar -jxf mpfr-3.1.3.tar.bz2
#cd mpfr-3.1.3/
#./configure
#make
#make install
#cd ~
#wget http://mirrors.ibiblio.org/gnu/ftp/gnu/mpc/mpc-1.0.3.tar.gz
#tar -zxf mpc-1.0.3.tar.gz
#cd mpc-1.0.3/
#./configure
#make
#make install
#
## Build gcc52
#cd ~
#wget http://mirrors.ibiblio.org/gnu/ftp/gnu/gcc/gcc-5.2.0/gcc-5.2.0.tar.bz2
#tar -jxf gcc-5.2.0.tar.bz2
#cd gcc-5.2.0
#mkdir objdir
#cd objdir
#../configure --disable-multilib
#make
#make install
#/sbin/ldconfig
#
# Boost
cd ~
wget http://downloads.sourceforge.net/boost/boost_1_57_0.tar.bz2
tar xjvf boost_1_57_0.tar.bz2
cd boost_1_57_0
#Safer to build all libraries as other projects rely upon aspects of boost
./bootstrap.sh --with-libraries=all
./bjam install
#cd ~; /bin/rm -rf ~/boost_1_55_0*

# SIGAR
cd ~
git clone git://github.com/hyperic/sigar.git
cd sigar
#Need the cppflag because of change in inline behavior
#https://github.com/hyperic/sigar/issues/60
./autogen.sh && ./configure CPPFLAGS='-fgnu89-inline' && make && make install
/sbin/ldconfig

# BerkeleyDB
cd ~
wget http://www.hypertable.com/uploads/db-4.8.26.tar.gz
tar -xzvf db-4.8.26.tar.gz
cd db-4.8.26/build_unix/
../dist/configure --enable-cxx
make
make install
sh -c "echo '/usr/local/BerkeleyDB.4.8/lib' > /etc/ld.so.conf.d/BerkeleyDB.4.8.conf"
cd ~; /bin/rm -rf ~/db-4.8.26*

# Google RE2
cd ~
wget http://www.hypertable.com/uploads/re2.tgz
tar -zxvf re2.tgz
cd re2
# insert the missing include (manifests as a missing "ptrdiff_t does not name a type" message)
sed 's:<string.h>:<string.h>\
#include <cstddef>:' re2/stringpiece.h > sp.bak && mv sp.bak re2/stringpiece.h
make
make install
#/bin/rm -rf ~/re2*

# libunwind
if [ $ARCH -eq 64 ]; then
  cd ~
  wget http://download.savannah.gnu.org/releases/libunwind/libunwind-1.0.1.tar.gz
  tar xzvf libunwind-1.0.1.tar.gz 
  cd libunwind-1.0.1/
  ./configure CFLAGS=-U_FORTIFY_SOURCE
  make
  make install
  cd ~
  /bin/rm -rf ~/libunwind-1.0.1*
fi

# Google Perftools
cd ~
wget http://google-perftools.googlecode.com/files/google-perftools-1.8.3.tar.gz
tar xzvf google-perftools-1.8.3.tar.gz
cd google-perftools-1.8.3
sed -i -e 's/siginfo_t/siginfo/' src/base/linuxthreads.cc
./configure
make
make install
cd ~
#/bin/rm -rf ~/google-perftools-1.8.3*


# Google Snappy
cd ~
wget http://snappy.googlecode.com/files/snappy-1.0.4.tar.gz
tar xzvf snappy-1.0.4.tar.gz
cd snappy-1.0.4
./configure
make
make install
cd ~
/bin/rm -rf ~/snappy-1.0.4*


# libevent4
#cd ~
#wget https://github.com/downloads/libevent/libevent/libevent-1.4.14b-stable.tar.gz
#tar xzvf libevent-1.4.14b-stable.tar.gz 
#cd libevent-1.4.14b-stable
#./configure 
#make
#make install
#cd ~; rm -rf libevent-1.4.14b-stable*

# libedit -need to compile separately for wide character support
cd ~
wget http://thrysoee.dk/editline/libedit-20150325-3.1.tar.gz
tar -zxf libedit-20150325-3.1.tar.gz
cd libedit-20150325-3.1
./configure --enable-widec
make
make install
cd ~

wget https://github.com/libevent/libevent/archive/master.zip
unzip master.zip
cd libevent-master
./autogen.sh
./configure
make
make install
cd ~;


# Scrooge (Scala Thrift binding)
wget http://apt.typesafe.com/repo-deb-build-0002.deb
dpkg -i repo-deb-build-0002.deb
apt-get update
apt-get -y --force-yes --fix-missing install sbt
mkdir -p ~/.sbt/.lib/0.11.2
cd ~/.sbt/.lib/0.11.2
wget http://typesafe.artifactoryonline.com/typesafe/ivy-releases/org.scala-tools.sbt/sbt-launch/0.11.2/sbt-launch.jar
cd /usr/src
git clone git://github.com/twitter/scrooge.git
cd scrooge
sbt -sbt-version 0.11.2 package-dist

apt-get -y --allow-unauthenticated install dstat doxygen rrdtool graphviz gdb emacs rdoc rubygems-integration capistrano

#Bundle needed for thrift/ruby extensions
gem install sinatra rack thin json titleize bundle

# Thrift
#Must be version 0.9.2, 0.9.3+ breaks the API HT uses with incrementRecursionDepth
#splitting from half-duplex to fullDuplex.
cd ~
#Mirror died?
#wget http://mirror.olnevhost.net/pub/apache/thrift/0.9.2/thrift-0.9.2.tar.gz
wget http://apache.mirror.anlx.net/thrift/0.9.3/thrift-0.9.3.tar.gz
tar xzvf thrift-0.9.3.tar.gz
cd thrift-0.9.3
#chmod 755 ./configure ./lib/php/src/ext/thrift_protocol/build/shtool
./configure --without-java
make
make install

cd ~
git clone https://github.com/jemalloc/jemalloc.git
cd jemalloc
./autogen.sh && ./configure && make && make install
cd ~

/sbin/ldconfig

