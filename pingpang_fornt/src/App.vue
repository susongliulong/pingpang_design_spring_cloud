<template>
  <div id="content">
      <RegisterVue></RegisterVue>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue"
import RegisterVue from '@/components/login_register/RegisterVue.vue'



</script>

<style>
* {
  padding: 0px;
  margin: 0px;
}

body::before {
  content: "";
  display: block;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0.5;
  z-index: -1;
  background-image: url("@/assets/login/background.png");
  background-repeat: no-repeat;
  background-position-x: center;

}
#content{
  margin-top: 20px;
  margin-left: 100px;
}

.center {
  position: absolute;
  width: 50%;
  height: 50%;
  transform: translate(-50%, -50%);
}

a {
  text-decoration: none;
  color: #109ce1;
}

input {
  outline: none;
  border: 1px #c1c5c8 solid;
}

.center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.left {
  float: left;
}

.right {
  float: right;
}

.clear::after {
  content: "";
  display: block;
  clear: both;
}
</style>
