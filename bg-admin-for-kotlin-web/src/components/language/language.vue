<template>
  <div>
    <Dropdown trigger="click" @on-click="selectLang">
      <a href="javascript:void(0)">
        {{ title }}
        <Icon :size="18" type="md-arrow-dropdown"/>
      </a>
      <DropdownMenu slot="list">
        <DropdownItem v-for="(value, key) in localList" :name="value.languageCode" :key="`lang-${key}`">{{
          value.languageName }}
        </DropdownItem>
      </DropdownMenu>
    </Dropdown>
  </div>
</template>

<script>

  import {getLanguageSelect} from '../../api/sys/language/language.api';

  export default {
    name: 'Language',
    props: {
      lang: String
    },
    data() {
      return {
        langList: [],
        localList: []
      }
    },
    watch: {
      lang(lang) {
        this.$i18n.locale = lang
      }
    },
    computed: {
      title() {
        for (let i = 0; i < this.langList.length; i++) {
          if (this.langList[i].languageCode == this.lang) {
            return this.langList[i].languageName;
          }
        }
      }
    },
    methods: {
      selectLang(name) {
        this.$emit('on-lang-change', name)
      },
      initLanguage() {
        let _this = this;
        getLanguageSelect({}).then(res => {
          if (res.code == 200) {
            res.obj.forEach(r => {
              let o = new Object();
              o.languageName = r.languageName;
              o.languageCode = r.languageCode;
              _this.langList.push(o);
              _this.localList.push(o);
            })
          }
        })
      }
    },
    mounted() {
      this.initLanguage();
    }
  }
</script>
