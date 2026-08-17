import{a as _t,c as se,d as ni,e as gt,f as oi,g as _i,h as gi}from"./chunk-DTULK7NQ.js";import{a as Ci,b as wi,c as Oi,d as Si,e as Ei,f as Mi,g as Ge,h as Pi,i as Ri,j as Di,k as Ii}from"./chunk-DITEEUJM.js";import{A as ki,C as xi,b as $t,d as fe,e as pt,n as ei,o as J,p as ti,q as ee,r as ii,s as ft,t as k,x as Xe,y as We,z as bi}from"./chunk-HX5HVGH6.js";import{a as ri,c as ai,d as si,e as V,f as ci,g as li,h as di,i as hi,k as mi,m as pi,n as qe,u as ui,w as fi}from"./chunk-6YALTE7V.js";import{a as Ai,b as Ti}from"./chunk-2XQLV74F.js";import{a as Ye,b as ut,d as He}from"./chunk-T4677QKP.js";import{b as Zt}from"./chunk-XQNXTJ4U.js";import{a as Oe,b as $,c as ae,d as Jt,f as vi,g as yi}from"./chunk-SAFXXSVB.js";import"./chunk-ZZHF54TS.js";import{b as Gt,e as Kt,t as Qt,u as Ut}from"./chunk-O5UCFPQR.js";import{$ as Ae,$a as Ve,$b as Le,A as Nt,Ab as a,Ac as Z,Ba as jt,Bb as b,Dc as v,Ec as we,Fb as K,Gb as Q,Gc as qt,Ib as xe,Jb as Ne,L as st,M as ct,Mb as x,N as de,O as Bt,Ob as y,Pb as U,Qb as H,Rb as Wt,S as Lt,Sb as pe,T as G,Ta as d,Tb as T,U as B,Ub as F,W as R,Y as p,Ya as dt,Yb as Be,Za as ht,Zb as ue,_a as Fe,_b as C,a as ie,ab as ye,ac as l,ba as L,bc as q,ca as z,cb as mt,cc as ze,d as P,da as Te,db as Yt,ea as zt,fa as j,ga as ne,gb as A,hb as Y,ib as ke,ja as w,ka as X,lb as E,lc as Ce,m as at,nb as Ht,oa as he,qb as Xt,r as Vt,rb as W,sa as me,sb as oe,ta as lt,tb as re,u as Ie,v as ve,va as D,wc as je,yb as _,zb as c}from"./chunk-47MLX6BQ.js";import{a as te,b as be,c as rt}from"./chunk-C6Q5SG76.js";var Se=class{_attachedHost=null;attach(i){return this._attachedHost=i,i.attach(this)}detach(){let i=this._attachedHost;i!=null&&(this._attachedHost=null,i.detach())}get isAttached(){return this._attachedHost!=null}setAttachedHost(i){this._attachedHost=i}},bt=class extends Se{component;viewContainerRef;injector;projectableNodes;bindings;constructor(i,e,t,n,r){super(),this.component=i,this.viewContainerRef=e,this.injector=t,this.projectableNodes=n,this.bindings=r||null}},Ee=class extends Se{templateRef;viewContainerRef;context;injector;constructor(i,e,t,n){super(),this.templateRef=i,this.viewContainerRef=e,this.context=t,this.injector=n}get origin(){return this.templateRef.elementRef}attach(i,e=this.context){return this.context=e,super.attach(i)}detach(){return this.context=void 0,super.detach()}},vt=class extends Se{element;constructor(i){super(),this.element=i instanceof D?i.nativeElement:i}},yt=class{_attachedPortal=null;_disposeFn=null;_isDisposed=!1;hasAttached(){return!!this._attachedPortal}attach(i){if(i instanceof bt)return this._attachedPortal=i,this.attachComponentPortal(i);if(i instanceof Ee)return this._attachedPortal=i,this.attachTemplatePortal(i);if(this.attachDomPortal&&i instanceof vt)return this._attachedPortal=i,this.attachDomPortal(i)}attachDomPortal=null;detach(){this._attachedPortal&&(this._attachedPortal.setAttachedHost(null),this._attachedPortal=null),this._invokeDisposeFn()}dispose(){this.hasAttached()&&this.detach(),this._invokeDisposeFn(),this._isDisposed=!0}setDisposeFn(i){this._disposeFn=i}_invokeDisposeFn(){this._disposeFn&&(this._disposeFn(),this._disposeFn=null)}},Ke=class extends yt{outletElement;_appRef;_defaultInjector;constructor(i,e,t){super(),this.outletElement=i,this._appRef=e,this._defaultInjector=t}attachComponentPortal(i){let e;if(i.viewContainerRef){let t=i.injector||i.viewContainerRef.injector,n=t.get(Yt,null,{optional:!0})||void 0;e=i.viewContainerRef.createComponent(i.component,{index:i.viewContainerRef.length,injector:t,ngModuleRef:n,projectableNodes:i.projectableNodes||void 0,bindings:i.bindings||void 0}),this.setDisposeFn(()=>e.destroy())}else{let t=this._appRef,n=i.injector||this._defaultInjector||j.NULL,r=n.get(Ae,t.injector);e=qt(i.component,{elementInjector:n,environmentInjector:r,projectableNodes:i.projectableNodes||void 0,bindings:i.bindings||void 0}),t.attachView(e.hostView),this.setDisposeFn(()=>{t.viewCount>0&&t.detachView(e.hostView),e.destroy()})}return this.outletElement.appendChild(this._getComponentRootNode(e)),this._attachedPortal=i,e}attachTemplatePortal(i){let e=i.viewContainerRef,t=e.createEmbeddedView(i.templateRef,i.context,{injector:i.injector});return t.rootNodes.forEach(n=>this.outletElement.appendChild(n)),t.detectChanges(),this.setDisposeFn(()=>{let n=e.indexOf(t);n!==-1&&e.remove(n)}),this._attachedPortal=i,t}attachDomPortal=i=>{let e=i.element;e.parentNode;let t=this.outletElement.ownerDocument.createComment("dom-portal");e.parentNode.insertBefore(t,e),this.outletElement.appendChild(e),this._attachedPortal=i,super.setDisposeFn(()=>{t.parentNode&&t.parentNode.replaceChild(e,t)})};dispose(){super.dispose(),this.outletElement.remove()}_getComponentRootNode(i){return i.hostView.rootNodes[0]}};var Fi=(()=>{class o{static \u0275fac=function(t){return new(t||o)};static \u0275mod=Y({type:o});static \u0275inj=B({})}return o})();var Vi=$t();function Xi(o){return new Qe(o.get(se),o.get(ne))}var Qe=class{_viewportRuler;_previousHTMLStyles={top:"",left:""};_previousScrollPosition;_isEnabled=!1;_document;constructor(i,e){this._viewportRuler=i,this._document=e}attach(){}enable(){if(this._canBeEnabled()){let i=this._document.documentElement;this._previousScrollPosition=this._viewportRuler.getViewportScrollPosition(),this._previousHTMLStyles.left=i.style.left||"",this._previousHTMLStyles.top=i.style.top||"",i.style.left=k(-this._previousScrollPosition.left),i.style.top=k(-this._previousScrollPosition.top),i.classList.add("cdk-global-scrollblock"),this._isEnabled=!0}}disable(){if(this._isEnabled){let i=this._document.documentElement,e=this._document.body,t=i.style,n=e.style,r=t.scrollBehavior||"",s=n.scrollBehavior||"";this._isEnabled=!1,t.left=this._previousHTMLStyles.left,t.top=this._previousHTMLStyles.top,i.classList.remove("cdk-global-scrollblock"),Vi&&(t.scrollBehavior=n.scrollBehavior="auto"),window.scroll(this._previousScrollPosition.left,this._previousScrollPosition.top),Vi&&(t.scrollBehavior=r,n.scrollBehavior=s)}}_canBeEnabled(){if(this._document.documentElement.classList.contains("cdk-global-scrollblock")||this._isEnabled)return!1;let e=this._document.documentElement,t=this._viewportRuler.getViewportSize();return e.scrollHeight>t.height||e.scrollWidth>t.width}};function Wi(o,i){return new Ue(o.get(_t),o.get(X),o.get(se),i)}var Ue=class{_scrollDispatcher;_ngZone;_viewportRuler;_config;_scrollSubscription=null;_overlayRef;_initialScrollPosition;constructor(i,e,t,n){this._scrollDispatcher=i,this._ngZone=e,this._viewportRuler=t,this._config=n}attach(i){this._overlayRef,this._overlayRef=i}enable(){if(this._scrollSubscription)return;let i=this._scrollDispatcher.scrolled(0).pipe(ve(e=>!e||!this._overlayRef.overlayElement.contains(e.getElementRef().nativeElement)));this._config&&this._config.threshold&&this._config.threshold>1?(this._initialScrollPosition=this._viewportRuler.getViewportScrollPosition().top,this._scrollSubscription=i.subscribe(()=>{let e=this._viewportRuler.getViewportScrollPosition().top;Math.abs(e-this._initialScrollPosition)>this._config.threshold?this._detach():this._overlayRef.updatePosition()})):this._scrollSubscription=i.subscribe(this._detach)}disable(){this._scrollSubscription&&(this._scrollSubscription.unsubscribe(),this._scrollSubscription=null)}detach(){this.disable(),this._overlayRef=null}_detach=()=>{this.disable(),this._overlayRef.hasAttached()&&this._ngZone.run(()=>this._overlayRef.detach())}};var Me=class{enable(){}disable(){}attach(){}};function kt(o,i){return i.some(e=>{let t=o.bottom<e.top,n=o.top>e.bottom,r=o.right<e.left,s=o.left>e.right;return t||n||r||s})}function Ni(o,i){return i.some(e=>{let t=o.top<e.top,n=o.bottom>e.bottom,r=o.left<e.left,s=o.right>e.right;return t||n||r||s})}function Re(o,i){return new Ze(o.get(_t),o.get(se),o.get(X),i)}var Ze=class{_scrollDispatcher;_viewportRuler;_ngZone;_config;_scrollSubscription=null;_overlayRef;constructor(i,e,t,n){this._scrollDispatcher=i,this._viewportRuler=e,this._ngZone=t,this._config=n}attach(i){this._overlayRef,this._overlayRef=i}enable(){if(!this._scrollSubscription){let i=this._config?this._config.scrollThrottle:0;this._scrollSubscription=this._scrollDispatcher.scrolled(i).subscribe(()=>{if(this._overlayRef.updatePosition(),this._config&&this._config.autoClose){let e=this._overlayRef.overlayElement.getBoundingClientRect(),{width:t,height:n}=this._viewportRuler.getViewportSize();kt(e,[{width:t,height:n,bottom:n,right:t,top:0,left:0}])&&(this.disable(),this._ngZone.run(()=>this._overlayRef.detach()))}})}}disable(){this._scrollSubscription&&(this._scrollSubscription.unsubscribe(),this._scrollSubscription=null)}detach(){this.disable(),this._overlayRef=null}},qi=(()=>{class o{_injector=p(j);constructor(){}noop=()=>new Me;close=e=>Wi(this._injector,e);block=()=>Xi(this._injector);reposition=e=>Re(this._injector,e);static \u0275fac=function(t){return new(t||o)};static \u0275prov=G({token:o,factory:o.\u0275fac,providedIn:"root"})}return o})(),Pe=class{positionStrategy;scrollStrategy=new Me;panelClass="";hasBackdrop=!1;backdropClass="cdk-overlay-dark-backdrop";disableAnimations;width;height;minWidth;minHeight;maxWidth;maxHeight;direction;disposeOnNavigation=!1;usePopover;eventPredicate;constructor(i){if(i){let e=Object.keys(i);for(let t of e)i[t]!==void 0&&(this[t]=i[t])}}};var $e=class{connectionPair;scrollableViewProperties;constructor(i,e){this.connectionPair=i,this.scrollableViewProperties=e}};var Gi=(()=>{class o{_attachedOverlays=[];_document=p(ne);_isAttached=!1;constructor(){}ngOnDestroy(){this.detach()}add(e){this.remove(e),this._attachedOverlays.push(e)}remove(e){let t=this._attachedOverlays.indexOf(e);t>-1&&this._attachedOverlays.splice(t,1),this._attachedOverlays.length===0&&this.detach()}canReceiveEvent(e,t,n){return n.observers.length<1?!1:e.eventPredicate?e.eventPredicate(t):!0}static \u0275fac=function(t){return new(t||o)};static \u0275prov=G({token:o,factory:o.\u0275fac,providedIn:"root"})}return o})(),Ki=(()=>{class o extends Gi{_ngZone=p(X);_renderer=p(Fe).createRenderer(null,null);_cleanupKeydown;add(e){super.add(e),this._isAttached||(this._ngZone.runOutsideAngular(()=>{this._cleanupKeydown=this._renderer.listen("body","keydown",this._keydownListener)}),this._isAttached=!0)}detach(){this._isAttached&&(this._cleanupKeydown?.(),this._isAttached=!1)}_keydownListener=e=>{let t=this._attachedOverlays;for(let n=t.length-1;n>-1;n--){let r=t[n];if(this.canReceiveEvent(r,e,r._keydownEvents)){this._ngZone.run(()=>r._keydownEvents.next(e));break}}};static \u0275fac=(()=>{let e;return function(n){return(e||(e=lt(o)))(n||o)}})();static \u0275prov=G({token:o,factory:o.\u0275fac,providedIn:"root"})}return o})(),Qi=(()=>{class o extends Gi{_platform=p(Ye);_ngZone=p(X);_renderer=p(Fe).createRenderer(null,null);_cursorOriginalValue;_cursorStyleIsSet=!1;_pointerDownEventTarget=null;_cleanups;add(e){if(super.add(e),!this._isAttached){let t=this._document.body,n={capture:!0},r=this._renderer;this._cleanups=this._ngZone.runOutsideAngular(()=>[r.listen(t,"pointerdown",this._pointerDownListener,n),r.listen(t,"click",this._clickListener,n),r.listen(t,"auxclick",this._clickListener,n),r.listen(t,"contextmenu",this._clickListener,n)]),this._platform.IOS&&!this._cursorStyleIsSet&&(this._cursorOriginalValue=t.style.cursor,t.style.cursor="pointer",this._cursorStyleIsSet=!0),this._isAttached=!0}}detach(){this._isAttached&&(this._cleanups?.forEach(e=>e()),this._cleanups=void 0,this._platform.IOS&&this._cursorStyleIsSet&&(this._document.body.style.cursor=this._cursorOriginalValue,this._cursorStyleIsSet=!1),this._isAttached=!1)}_pointerDownListener=e=>{this._pointerDownEventTarget=fe(e)};_clickListener=e=>{let t=fe(e),n=e.type==="click"&&this._pointerDownEventTarget?this._pointerDownEventTarget:t;this._pointerDownEventTarget=null;let r=this._attachedOverlays.slice();for(let s=r.length-1;s>-1;s--){let h=r[s],m=h._outsidePointerEvents;if(!(!h.hasAttached()||!this.canReceiveEvent(h,e,m))){if(Bi(h.overlayElement,t)||Bi(h.overlayElement,n))break;this._ngZone?this._ngZone.run(()=>m.next(e)):m.next(e)}}};static \u0275fac=(()=>{let e;return function(n){return(e||(e=lt(o)))(n||o)}})();static \u0275prov=G({token:o,factory:o.\u0275fac,providedIn:"root"})}return o})();function Bi(o,i){let e=typeof ShadowRoot<"u"&&ShadowRoot,t=i;for(;t;){if(t===o)return!0;t=e&&t instanceof ShadowRoot?t.host:t.parentNode}return!1}var Ui=(()=>{class o{static \u0275fac=function(t){return new(t||o)};static \u0275cmp=A({type:o,selectors:[["ng-component"]],hostAttrs:["cdk-overlay-style-loader",""],decls:0,vars:0,template:function(t,n){},styles:[`.cdk-overlay-container, .cdk-global-overlay-wrapper {
  pointer-events: none;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
}

.cdk-overlay-container {
  position: fixed;
}
@layer cdk-overlay {
  .cdk-overlay-container {
    z-index: 1000;
  }
}
.cdk-overlay-container:empty {
  display: none;
}

.cdk-global-overlay-wrapper {
  display: flex;
  position: absolute;
}
@layer cdk-overlay {
  .cdk-global-overlay-wrapper {
    z-index: 1000;
  }
}

.cdk-overlay-pane {
  position: absolute;
  pointer-events: auto;
  box-sizing: border-box;
  display: flex;
  max-width: 100%;
  max-height: 100%;
}
@layer cdk-overlay {
  .cdk-overlay-pane {
    z-index: 1000;
  }
}

.cdk-overlay-backdrop {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  pointer-events: auto;
  -webkit-tap-highlight-color: transparent;
  opacity: 0;
  touch-action: manipulation;
}
@layer cdk-overlay {
  .cdk-overlay-backdrop {
    z-index: 1000;
    transition: opacity 400ms cubic-bezier(0.25, 0.8, 0.25, 1);
  }
}
@media (prefers-reduced-motion) {
  .cdk-overlay-backdrop {
    transition-duration: 1ms;
  }
}

.cdk-overlay-backdrop-showing {
  opacity: 1;
}
@media (forced-colors: active) {
  .cdk-overlay-backdrop-showing {
    opacity: 0.6;
  }
}

@layer cdk-overlay {
  .cdk-overlay-dark-backdrop {
    background: rgba(0, 0, 0, 0.32);
  }
}

.cdk-overlay-transparent-backdrop {
  transition: visibility 1ms linear, opacity 1ms linear;
  visibility: hidden;
  opacity: 1;
}
.cdk-overlay-transparent-backdrop.cdk-overlay-backdrop-showing, .cdk-high-contrast-active .cdk-overlay-transparent-backdrop {
  opacity: 0;
  visibility: visible;
}

.cdk-overlay-backdrop-noop-animation {
  transition: none;
}

.cdk-overlay-connected-position-bounding-box {
  position: absolute;
  display: flex;
  flex-direction: column;
  min-width: 1px;
  min-height: 1px;
}
@layer cdk-overlay {
  .cdk-overlay-connected-position-bounding-box {
    z-index: 1000;
  }
}

.cdk-global-scrollblock {
  position: fixed;
  width: 100%;
  overflow-y: scroll;
}

.cdk-overlay-popover {
  background: none;
  border: none;
  padding: 0;
  outline: 0;
  overflow: visible;
  position: fixed;
  pointer-events: none;
  white-space: normal;
  color: inherit;
  text-decoration: none;
  width: 100%;
  height: 100%;
  inset: auto;
  top: 0;
  left: 0;
}
.cdk-overlay-popover::backdrop {
  display: none;
}
.cdk-overlay-popover .cdk-overlay-backdrop {
  position: fixed;
  z-index: auto;
}
`],encapsulation:2,changeDetection:0})}return o})(),Zi=(()=>{class o{_platform=p(Ye);_containerElement;_document=p(ne);_styleLoader=p(ae);constructor(){}ngOnDestroy(){this._containerElement?.remove()}getContainerElement(){return this._loadStyles(),this._containerElement||this._createContainer(),this._containerElement}_createContainer(){let e="cdk-overlay-container";if(this._platform.isBrowser||pt()){let n=this._document.querySelectorAll(`.${e}[platform="server"], .${e}[platform="test"]`);for(let r=0;r<n.length;r++)n[r].remove()}let t=this._document.createElement("div");t.classList.add(e),pt()?t.setAttribute("platform","test"):this._platform.isBrowser||t.setAttribute("platform","server"),this._document.body.appendChild(t),this._containerElement=t}_loadStyles(){this._styleLoader.load(Ui)}static \u0275fac=function(t){return new(t||o)};static \u0275prov=G({token:o,factory:o.\u0275fac,providedIn:"root"})}return o})(),xt=class{_renderer;_ngZone;element;_cleanupClick;_cleanupTransitionEnd;_fallbackTimeout;constructor(i,e,t,n){this._renderer=e,this._ngZone=t,this.element=i.createElement("div"),this.element.classList.add("cdk-overlay-backdrop"),this._cleanupClick=e.listen(this.element,"click",n)}detach(){this._ngZone.runOutsideAngular(()=>{let i=this.element;clearTimeout(this._fallbackTimeout),this._cleanupTransitionEnd?.(),this._cleanupTransitionEnd=this._renderer.listen(i,"transitionend",this.dispose),this._fallbackTimeout=setTimeout(this.dispose,500),i.style.pointerEvents="none",i.classList.remove("cdk-overlay-backdrop-showing")})}dispose=()=>{clearTimeout(this._fallbackTimeout),this._cleanupClick?.(),this._cleanupTransitionEnd?.(),this._cleanupClick=this._cleanupTransitionEnd=this._fallbackTimeout=void 0,this.element.remove()}};function Ct(o){return o&&o.nodeType===1}var Je=class{_portalOutlet;_host;_pane;_config;_ngZone;_keyboardDispatcher;_document;_location;_outsideClickDispatcher;_animationsDisabled;_injector;_renderer;_backdropClick=new P;_attachments=new P;_detachments=new P;_positionStrategy;_scrollStrategy;_locationChanges=ie.EMPTY;_backdropRef=null;_detachContentMutationObserver;_detachContentAfterRenderRef;_disposed=!1;_previousHostParent;_keydownEvents=new P;_outsidePointerEvents=new P;_afterNextRenderRef;constructor(i,e,t,n,r,s,h,m,g,f=!1,u,S){this._portalOutlet=i,this._host=e,this._pane=t,this._config=n,this._ngZone=r,this._keyboardDispatcher=s,this._document=h,this._location=m,this._outsideClickDispatcher=g,this._animationsDisabled=f,this._injector=u,this._renderer=S,n.scrollStrategy&&(this._scrollStrategy=n.scrollStrategy,this._scrollStrategy.attach(this)),this._positionStrategy=n.positionStrategy}get overlayElement(){return this._pane}get backdropElement(){return this._backdropRef?.element||null}get hostElement(){return this._host}get eventPredicate(){return this._config?.eventPredicate||null}attach(i){if(this._disposed)return null;this._attachHost();let e=this._portalOutlet.attach(i);return this._positionStrategy?.attach(this),this._updateStackingOrder(),this._updateElementSize(),this._updateElementDirection(),this._scrollStrategy&&this._scrollStrategy.enable(),this._afterNextRenderRef?.destroy(),this._afterNextRenderRef=dt(()=>{this.hasAttached()&&this.updatePosition()},{injector:this._injector}),this._togglePointerEvents(!0),this._config.hasBackdrop&&this._attachBackdrop(),this._config.panelClass&&this._toggleClasses(this._pane,this._config.panelClass,!0),this._attachments.next(),this._completeDetachContent(),this._keyboardDispatcher.add(this),this._config.disposeOnNavigation&&(this._locationChanges=this._location.subscribe(()=>this.dispose())),this._outsideClickDispatcher.add(this),typeof e?.onDestroy=="function"&&e.onDestroy(()=>{this.hasAttached()&&this._ngZone.runOutsideAngular(()=>Promise.resolve().then(()=>this.detach()))}),e}detach(){if(!this.hasAttached())return;this.detachBackdrop(),this._togglePointerEvents(!1),this._positionStrategy&&this._positionStrategy.detach&&this._positionStrategy.detach(),this._scrollStrategy&&this._scrollStrategy.disable();let i=this._portalOutlet.detach();return this._detachments.next(),this._completeDetachContent(),this._keyboardDispatcher.remove(this),this._detachContentWhenEmpty(),this._locationChanges.unsubscribe(),this._outsideClickDispatcher.remove(this),i}dispose(){if(this._disposed)return;let i=this.hasAttached();this._positionStrategy&&this._positionStrategy.dispose(),this._disposeScrollStrategy(),this._backdropRef?.dispose(),this._locationChanges.unsubscribe(),this._keyboardDispatcher.remove(this),this._portalOutlet.dispose(),this._attachments.complete(),this._backdropClick.complete(),this._keydownEvents.complete(),this._outsidePointerEvents.complete(),this._outsideClickDispatcher.remove(this),this._host?.remove(),this._afterNextRenderRef?.destroy(),this._previousHostParent=this._pane=this._host=this._backdropRef=null,i&&this._detachments.next(),this._detachments.complete(),this._completeDetachContent(),this._disposed=!0}hasAttached(){return this._portalOutlet.hasAttached()}backdropClick(){return this._backdropClick}attachments(){return this._attachments}detachments(){return this._detachments}keydownEvents(){return this._keydownEvents}outsidePointerEvents(){return this._outsidePointerEvents}getConfig(){return this._config}updatePosition(){this._positionStrategy&&this._positionStrategy.apply()}updatePositionStrategy(i){i!==this._positionStrategy&&(this._positionStrategy&&this._positionStrategy.dispose(),this._positionStrategy=i,this.hasAttached()&&(i.attach(this),this.updatePosition()))}updateSize(i){this._config=te(te({},this._config),i),this._updateElementSize()}setDirection(i){this._config=be(te({},this._config),{direction:i}),this._updateElementDirection()}addPanelClass(i){this._pane&&this._toggleClasses(this._pane,i,!0)}removePanelClass(i){this._pane&&this._toggleClasses(this._pane,i,!1)}getDirection(){let i=this._config.direction;return i?typeof i=="string"?i:i.value:"ltr"}updateScrollStrategy(i){i!==this._scrollStrategy&&(this._disposeScrollStrategy(),this._scrollStrategy=i,this.hasAttached()&&(i.attach(this),i.enable()))}_updateElementDirection(){this._host.setAttribute("dir",this.getDirection())}_updateElementSize(){if(!this._pane)return;let i=this._pane.style;i.width=k(this._config.width),i.height=k(this._config.height),i.minWidth=k(this._config.minWidth),i.minHeight=k(this._config.minHeight),i.maxWidth=k(this._config.maxWidth),i.maxHeight=k(this._config.maxHeight)}_togglePointerEvents(i){this._pane.style.pointerEvents=i?"":"none"}_attachHost(){if(!this._host.parentElement){let i=this._config.usePopover?this._positionStrategy?.getPopoverInsertionPoint?.():null;Ct(i)?i.after(this._host):i?.type==="parent"?i.element.appendChild(this._host):this._previousHostParent?.appendChild(this._host)}if(this._config.usePopover)try{this._host.showPopover()}catch{}}_attachBackdrop(){let i="cdk-overlay-backdrop-showing";this._backdropRef?.dispose(),this._backdropRef=new xt(this._document,this._renderer,this._ngZone,e=>{this._backdropClick.next(e)}),this._animationsDisabled&&this._backdropRef.element.classList.add("cdk-overlay-backdrop-noop-animation"),this._config.backdropClass&&this._toggleClasses(this._backdropRef.element,this._config.backdropClass,!0),this._config.usePopover?this._host.prepend(this._backdropRef.element):this._host.parentElement.insertBefore(this._backdropRef.element,this._host),!this._animationsDisabled&&typeof requestAnimationFrame<"u"?this._ngZone.runOutsideAngular(()=>{requestAnimationFrame(()=>this._backdropRef?.element.classList.add(i))}):this._backdropRef.element.classList.add(i)}_updateStackingOrder(){!this._config.usePopover&&this._host.nextSibling&&this._host.parentNode.appendChild(this._host)}detachBackdrop(){this._animationsDisabled?(this._backdropRef?.dispose(),this._backdropRef=null):this._backdropRef?.detach()}_toggleClasses(i,e,t){let n=ut(e||[]).filter(r=>!!r);n.length&&(t?i.classList.add(...n):i.classList.remove(...n))}_detachContentWhenEmpty(){let i=!1;try{this._detachContentAfterRenderRef=dt(()=>{i=!0,this._detachContent()},{injector:this._injector})}catch(e){if(i)throw e;this._detachContent()}globalThis.MutationObserver&&this._pane&&(this._detachContentMutationObserver||=new globalThis.MutationObserver(()=>{this._detachContent()}),this._detachContentMutationObserver.observe(this._pane,{childList:!0}))}_detachContent(){(!this._pane||!this._host||this._pane.children.length===0)&&(this._pane&&this._config.panelClass&&this._toggleClasses(this._pane,this._config.panelClass,!1),this._host&&this._host.parentElement&&(this._previousHostParent=this._host.parentElement,this._host.remove()),this._completeDetachContent())}_completeDetachContent(){this._detachContentAfterRenderRef?.destroy(),this._detachContentAfterRenderRef=void 0,this._detachContentMutationObserver?.disconnect()}_disposeScrollStrategy(){let i=this._scrollStrategy;i?.disable(),i?.detach?.()}},Li="cdk-overlay-connected-position-bounding-box",mn=/([A-Za-z%]+)$/;function wt(o,i){return new et(i,o.get(se),o.get(ne),o.get(Ye),o.get(Zi))}var et=class{_viewportRuler;_document;_platform;_overlayContainer;_overlayRef;_isInitialRender=!1;_lastBoundingBoxSize={width:0,height:0};_isPushed=!1;_canPush=!0;_growAfterOpen=!1;_hasFlexibleDimensions=!0;_positionLocked=!1;_originRect;_overlayRect;_viewportRect;_containerRect;_viewportMargin=0;_scrollables=[];_preferredPositions=[];_origin;_pane;_isDisposed=!1;_boundingBox=null;_lastPosition=null;_lastScrollVisibility=null;_positionChanges=new P;_resizeSubscription=ie.EMPTY;_offsetX=0;_offsetY=0;_transformOriginSelector;_appliedPanelClasses=[];_previousPushAmount=null;_popoverLocation="global";positionChanges=this._positionChanges;get positions(){return this._preferredPositions}constructor(i,e,t,n,r){this._viewportRuler=e,this._document=t,this._platform=n,this._overlayContainer=r,this.setOrigin(i)}attach(i){this._overlayRef&&this._overlayRef,this._validatePositions(),i.hostElement.classList.add(Li),this._overlayRef=i,this._boundingBox=i.hostElement,this._pane=i.overlayElement,this._isDisposed=!1,this._isInitialRender=!0,this._lastPosition=null,this._resizeSubscription.unsubscribe(),this._resizeSubscription=this._viewportRuler.change().subscribe(()=>{this._isInitialRender=!0,this.apply()})}apply(){if(this._isDisposed||!this._platform.isBrowser)return;if(!this._isInitialRender&&this._positionLocked&&this._lastPosition){this.reapplyLastPosition();return}this._clearPanelClasses(),this._resetOverlayElementStyles(),this._resetBoundingBoxStyles(),this._viewportRect=this._getNarrowedViewportRect(),this._originRect=this._getOriginRect(),this._overlayRect=this._pane.getBoundingClientRect(),this._containerRect=this._getContainerRect();let i=this._originRect,e=this._overlayRect,t=this._viewportRect,n=this._containerRect,r=[],s;for(let h of this._preferredPositions){let m=this._getOriginPoint(i,n,h),g=this._getOverlayPoint(m,e,h),f=this._getOverlayFit(g,e,t,h);if(f.isCompletelyWithinViewport){this._isPushed=!1,this._applyPosition(h,m);return}if(this._canFitWithFlexibleDimensions(f,g,t)){r.push({position:h,origin:m,overlayRect:e,boundingBoxRect:this._calculateBoundingBoxRect(m,h)});continue}(!s||s.overlayFit.visibleArea<f.visibleArea)&&(s={overlayFit:f,overlayPoint:g,originPoint:m,position:h,overlayRect:e})}if(r.length){let h=null,m=-1;for(let g of r){let f=g.boundingBoxRect.width*g.boundingBoxRect.height*(g.position.weight||1);f>m&&(m=f,h=g)}this._isPushed=!1,this._applyPosition(h.position,h.origin);return}if(this._canPush){this._isPushed=!0,this._applyPosition(s.position,s.originPoint);return}this._applyPosition(s.position,s.originPoint)}detach(){this._clearPanelClasses(),this._lastPosition=null,this._previousPushAmount=null,this._resizeSubscription.unsubscribe()}dispose(){this._isDisposed||(this._boundingBox&&le(this._boundingBox.style,{top:"",left:"",right:"",bottom:"",height:"",width:"",alignItems:"",justifyContent:""}),this._pane&&this._resetOverlayElementStyles(),this._overlayRef&&this._overlayRef.hostElement.classList.remove(Li),this.detach(),this._positionChanges.complete(),this._overlayRef=this._boundingBox=null,this._isDisposed=!0)}reapplyLastPosition(){if(this._isDisposed||!this._platform.isBrowser)return;let i=this._lastPosition;i?(this._originRect=this._getOriginRect(),this._overlayRect=this._pane.getBoundingClientRect(),this._viewportRect=this._getNarrowedViewportRect(),this._containerRect=this._getContainerRect(),this._applyPosition(i,this._getOriginPoint(this._originRect,this._containerRect,i))):this.apply()}withScrollableContainers(i){return this._scrollables=i,this}withPositions(i){return this._preferredPositions=i,i.indexOf(this._lastPosition)===-1&&(this._lastPosition=null),this._validatePositions(),this}withViewportMargin(i){return this._viewportMargin=i,this}withFlexibleDimensions(i=!0){return this._hasFlexibleDimensions=i,this}withGrowAfterOpen(i=!0){return this._growAfterOpen=i,this}withPush(i=!0){return this._canPush=i,this}withLockedPosition(i=!0){return this._positionLocked=i,this}setOrigin(i){return this._origin=i,this}withDefaultOffsetX(i){return this._offsetX=i,this}withDefaultOffsetY(i){return this._offsetY=i,this}withTransformOriginOn(i){return this._transformOriginSelector=i,this}withPopoverLocation(i){return this._popoverLocation=i,this}getPopoverInsertionPoint(){return this._popoverLocation==="global"?null:this._popoverLocation!=="inline"?this._popoverLocation:this._origin instanceof D?this._origin.nativeElement:Ct(this._origin)?this._origin:null}_getOriginPoint(i,e,t){let n;if(t.originX=="center")n=i.left+i.width/2;else{let s=this._isRtl()?i.right:i.left,h=this._isRtl()?i.left:i.right;n=t.originX=="start"?s:h}e.left<0&&(n-=e.left);let r;return t.originY=="center"?r=i.top+i.height/2:r=t.originY=="top"?i.top:i.bottom,e.top<0&&(r-=e.top),{x:n,y:r}}_getOverlayPoint(i,e,t){let n;t.overlayX=="center"?n=-e.width/2:t.overlayX==="start"?n=this._isRtl()?-e.width:0:n=this._isRtl()?0:-e.width;let r;return t.overlayY=="center"?r=-e.height/2:r=t.overlayY=="top"?0:-e.height,{x:i.x+n,y:i.y+r}}_getOverlayFit(i,e,t,n){let r=ji(e),{x:s,y:h}=i,m=this._getOffset(n,"x"),g=this._getOffset(n,"y");m&&(s+=m),g&&(h+=g);let f=0-s,u=s+r.width-t.width,S=0-h,I=h+r.height-t.height,M=this._subtractOverflows(r.width,f,u),N=this._subtractOverflows(r.height,S,I),Ft=M*N;return{visibleArea:Ft,isCompletelyWithinViewport:r.width*r.height===Ft,fitsInViewportVertically:N===r.height,fitsInViewportHorizontally:M==r.width}}_canFitWithFlexibleDimensions(i,e,t){if(this._hasFlexibleDimensions){let n=t.bottom-e.y,r=t.right-e.x,s=zi(this._overlayRef.getConfig().minHeight),h=zi(this._overlayRef.getConfig().minWidth),m=i.fitsInViewportVertically||s!=null&&s<=n,g=i.fitsInViewportHorizontally||h!=null&&h<=r;return m&&g}return!1}_pushOverlayOnScreen(i,e,t){if(this._previousPushAmount&&this._positionLocked)return{x:i.x+this._previousPushAmount.x,y:i.y+this._previousPushAmount.y};let n=ji(e),r=this._viewportRect,s=Math.max(i.x+n.width-r.width,0),h=Math.max(i.y+n.height-r.height,0),m=Math.max(r.top-t.top-i.y,0),g=Math.max(r.left-t.left-i.x,0),f=0,u=0;return n.width<=r.width?f=g||-s:f=i.x<this._getViewportMarginStart()?r.left-t.left-i.x:0,n.height<=r.height?u=m||-h:u=i.y<this._getViewportMarginTop()?r.top-t.top-i.y:0,this._previousPushAmount={x:f,y:u},{x:i.x+f,y:i.y+u}}_applyPosition(i,e){if(this._setTransformOrigin(i),this._setOverlayElementStyles(e,i),this._setBoundingBoxStyles(e,i),i.panelClass&&this._addPanelClasses(i.panelClass),this._positionChanges.observers.length){let t=this._getScrollVisibility();if(i!==this._lastPosition||!this._lastScrollVisibility||!pn(this._lastScrollVisibility,t)){let n=new $e(i,t);this._positionChanges.next(n)}this._lastScrollVisibility=t}this._lastPosition=i,this._isInitialRender=!1}_setTransformOrigin(i){if(!this._transformOriginSelector)return;let e=this._boundingBox.querySelectorAll(this._transformOriginSelector),t,n=i.overlayY;i.overlayX==="center"?t="center":this._isRtl()?t=i.overlayX==="start"?"right":"left":t=i.overlayX==="start"?"left":"right";for(let r=0;r<e.length;r++)e[r].style.transformOrigin=`${t} ${n}`}_calculateBoundingBoxRect(i,e){let t=this._viewportRect,n=this._isRtl(),r,s,h;if(e.overlayY==="top")s=i.y,r=t.height-s+this._getViewportMarginBottom();else if(e.overlayY==="bottom")h=t.height-i.y+this._getViewportMarginTop()+this._getViewportMarginBottom(),r=t.height-h+this._getViewportMarginTop();else{let I=Math.min(t.bottom-i.y+t.top,i.y),M=this._lastBoundingBoxSize.height;r=I*2,s=i.y-I,r>M&&!this._isInitialRender&&!this._growAfterOpen&&(s=i.y-M/2)}let m=e.overlayX==="start"&&!n||e.overlayX==="end"&&n,g=e.overlayX==="end"&&!n||e.overlayX==="start"&&n,f,u,S;if(g)S=t.width-i.x+this._getViewportMarginStart()+this._getViewportMarginEnd(),f=i.x-this._getViewportMarginStart();else if(m)u=i.x,f=t.right-i.x-this._getViewportMarginEnd();else{let I=Math.min(t.right-i.x+t.left,i.x),M=this._lastBoundingBoxSize.width;f=I*2,u=i.x-I,f>M&&!this._isInitialRender&&!this._growAfterOpen&&(u=i.x-M/2)}return{top:s,left:u,bottom:h,right:S,width:f,height:r}}_setBoundingBoxStyles(i,e){let t=this._calculateBoundingBoxRect(i,e);!this._isInitialRender&&!this._growAfterOpen&&(t.height=Math.min(t.height,this._lastBoundingBoxSize.height),t.width=Math.min(t.width,this._lastBoundingBoxSize.width));let n={};if(this._hasExactPosition())n.top=n.left="0",n.bottom=n.right="auto",n.maxHeight=n.maxWidth="",n.width=n.height="100%";else{let r=this._overlayRef.getConfig().maxHeight,s=this._overlayRef.getConfig().maxWidth;n.width=k(t.width),n.height=k(t.height),n.top=k(t.top)||"auto",n.bottom=k(t.bottom)||"auto",n.left=k(t.left)||"auto",n.right=k(t.right)||"auto",e.overlayX==="center"?n.alignItems="center":n.alignItems=e.overlayX==="end"?"flex-end":"flex-start",e.overlayY==="center"?n.justifyContent="center":n.justifyContent=e.overlayY==="bottom"?"flex-end":"flex-start",r&&(n.maxHeight=k(r)),s&&(n.maxWidth=k(s))}this._lastBoundingBoxSize=t,le(this._boundingBox.style,n)}_resetBoundingBoxStyles(){le(this._boundingBox.style,{top:"0",left:"0",right:"0",bottom:"0",height:"",width:"",alignItems:"",justifyContent:""})}_resetOverlayElementStyles(){le(this._pane.style,{top:"",left:"",bottom:"",right:"",position:"",transform:""})}_setOverlayElementStyles(i,e){let t={},n=this._hasExactPosition(),r=this._hasFlexibleDimensions,s=this._overlayRef.getConfig();if(n){let f=this._viewportRuler.getViewportScrollPosition();le(t,this._getExactOverlayY(e,i,f)),le(t,this._getExactOverlayX(e,i,f))}else t.position="static";let h="",m=this._getOffset(e,"x"),g=this._getOffset(e,"y");m&&(h+=`translateX(${m}px) `),g&&(h+=`translateY(${g}px)`),t.transform=h.trim(),s.maxHeight&&(n?t.maxHeight=k(s.maxHeight):r&&(t.maxHeight="")),s.maxWidth&&(n?t.maxWidth=k(s.maxWidth):r&&(t.maxWidth="")),le(this._pane.style,t)}_getExactOverlayY(i,e,t){let n={top:"",bottom:""},r=this._getOverlayPoint(e,this._overlayRect,i);if(this._isPushed&&(r=this._pushOverlayOnScreen(r,this._overlayRect,t)),i.overlayY==="bottom"){let s=this._document.documentElement.clientHeight;n.bottom=`${s-(r.y+this._overlayRect.height)}px`}else n.top=k(r.y);return n}_getExactOverlayX(i,e,t){let n={left:"",right:""},r=this._getOverlayPoint(e,this._overlayRect,i);this._isPushed&&(r=this._pushOverlayOnScreen(r,this._overlayRect,t));let s;if(this._isRtl()?s=i.overlayX==="end"?"left":"right":s=i.overlayX==="end"?"right":"left",s==="right"){let h=this._document.documentElement.clientWidth;n.right=`${h-(r.x+this._overlayRect.width)}px`}else n.left=k(r.x);return n}_getScrollVisibility(){let i=this._getOriginRect(),e=this._pane.getBoundingClientRect(),t=this._scrollables.map(n=>n.getElementRef().nativeElement.getBoundingClientRect());return{isOriginClipped:Ni(i,t),isOriginOutsideView:kt(i,t),isOverlayClipped:Ni(e,t),isOverlayOutsideView:kt(e,t)}}_subtractOverflows(i,...e){return e.reduce((t,n)=>t-Math.max(n,0),i)}_getNarrowedViewportRect(){let i=this._document.documentElement.clientWidth,e=this._document.documentElement.clientHeight,t=this._viewportRuler.getViewportScrollPosition();return{top:t.top+this._getViewportMarginTop(),left:t.left+this._getViewportMarginStart(),right:t.left+i-this._getViewportMarginEnd(),bottom:t.top+e-this._getViewportMarginBottom(),width:i-this._getViewportMarginStart()-this._getViewportMarginEnd(),height:e-this._getViewportMarginTop()-this._getViewportMarginBottom()}}_isRtl(){return this._overlayRef.getDirection()==="rtl"}_hasExactPosition(){return!this._hasFlexibleDimensions||this._isPushed}_getOffset(i,e){return e==="x"?i.offsetX==null?this._offsetX:i.offsetX:i.offsetY==null?this._offsetY:i.offsetY}_validatePositions(){}_addPanelClasses(i){this._pane&&ut(i).forEach(e=>{e!==""&&this._appliedPanelClasses.indexOf(e)===-1&&(this._appliedPanelClasses.push(e),this._pane.classList.add(e))})}_clearPanelClasses(){this._pane&&(this._appliedPanelClasses.forEach(i=>{this._pane.classList.remove(i)}),this._appliedPanelClasses=[])}_getViewportMarginStart(){return typeof this._viewportMargin=="number"?this._viewportMargin:this._viewportMargin?.start??0}_getViewportMarginEnd(){return typeof this._viewportMargin=="number"?this._viewportMargin:this._viewportMargin?.end??0}_getViewportMarginTop(){return typeof this._viewportMargin=="number"?this._viewportMargin:this._viewportMargin?.top??0}_getViewportMarginBottom(){return typeof this._viewportMargin=="number"?this._viewportMargin:this._viewportMargin?.bottom??0}_getOriginRect(){let i=this._origin;if(i instanceof D)return i.nativeElement.getBoundingClientRect();if(i instanceof Element)return i.getBoundingClientRect();let e=i.width||0,t=i.height||0;return{top:i.y,bottom:i.y+t,left:i.x,right:i.x+e,height:t,width:e}}_getContainerRect(){let i=this._overlayRef.getConfig().usePopover&&this._popoverLocation!=="global",e=this._overlayContainer.getContainerElement();i&&(e.style.display="block");let t=e.getBoundingClientRect();return i&&(e.style.display=""),t}};function le(o,i){for(let e in i)i.hasOwnProperty(e)&&(o[e]=i[e]);return o}function zi(o){if(typeof o!="number"&&o!=null){let[i,e]=o.split(mn);return!e||e==="px"?parseFloat(i):null}return o||null}function ji(o){return{top:Math.floor(o.top),right:Math.floor(o.right),bottom:Math.floor(o.bottom),left:Math.floor(o.left),width:Math.floor(o.width),height:Math.floor(o.height)}}function pn(o,i){return o===i?!0:o.isOriginClipped===i.isOriginClipped&&o.isOriginOutsideView===i.isOriginOutsideView&&o.isOverlayClipped===i.isOverlayClipped&&o.isOverlayOutsideView===i.isOverlayOutsideView}var Yi="cdk-global-overlay-wrapper";function $i(o){return new tt}var tt=class{_overlayRef;_cssPosition="static";_topOffset="";_bottomOffset="";_alignItems="";_xPosition="";_xOffset="";_width="";_height="";_isDisposed=!1;attach(i){let e=i.getConfig();this._overlayRef=i,this._width&&!e.width&&i.updateSize({width:this._width}),this._height&&!e.height&&i.updateSize({height:this._height}),i.hostElement.classList.add(Yi),this._isDisposed=!1}top(i=""){return this._bottomOffset="",this._topOffset=i,this._alignItems="flex-start",this}left(i=""){return this._xOffset=i,this._xPosition="left",this}bottom(i=""){return this._topOffset="",this._bottomOffset=i,this._alignItems="flex-end",this}right(i=""){return this._xOffset=i,this._xPosition="right",this}start(i=""){return this._xOffset=i,this._xPosition="start",this}end(i=""){return this._xOffset=i,this._xPosition="end",this}width(i=""){return this._overlayRef?this._overlayRef.updateSize({width:i}):this._width=i,this}height(i=""){return this._overlayRef?this._overlayRef.updateSize({height:i}):this._height=i,this}centerHorizontally(i=""){return this.left(i),this._xPosition="center",this}centerVertically(i=""){return this.top(i),this._alignItems="center",this}apply(){if(!this._overlayRef||!this._overlayRef.hasAttached())return;let i=this._overlayRef.overlayElement.style,e=this._overlayRef.hostElement.style,t=this._overlayRef.getConfig(),{width:n,height:r,maxWidth:s,maxHeight:h}=t,m=(n==="100%"||n==="100vw")&&(!s||s==="100%"||s==="100vw"),g=(r==="100%"||r==="100vh")&&(!h||h==="100%"||h==="100vh"),f=this._xPosition,u=this._xOffset,S=this._overlayRef.getConfig().direction==="rtl",I="",M="",N="";m?N="flex-start":f==="center"?(N="center",S?M=u:I=u):S?f==="left"||f==="end"?(N="flex-end",I=u):(f==="right"||f==="start")&&(N="flex-start",M=u):f==="left"||f==="start"?(N="flex-start",I=u):(f==="right"||f==="end")&&(N="flex-end",M=u),i.position=this._cssPosition,i.marginLeft=m?"0":I,i.marginTop=g?"0":this._topOffset,i.marginBottom=this._bottomOffset,i.marginRight=m?"0":M,e.justifyContent=N,e.alignItems=g?"flex-start":this._alignItems}dispose(){if(this._isDisposed||!this._overlayRef)return;let i=this._overlayRef.overlayElement.style,e=this._overlayRef.hostElement,t=e.style;e.classList.remove(Yi),t.justifyContent=t.alignItems=i.marginTop=i.marginBottom=i.marginLeft=i.marginRight=i.position="",this._overlayRef=null,this._isDisposed=!0}},Ji=(()=>{class o{_injector=p(j);constructor(){}global(){return $i()}flexibleConnectedTo(e){return wt(this._injector,e)}static \u0275fac=function(t){return new(t||o)};static \u0275prov=G({token:o,factory:o.\u0275fac,providedIn:"root"})}return o})(),De=new R("OVERLAY_DEFAULT_CONFIG");function Ot(o,i){o.get(ae).load(Ui);let e=o.get(Zi),t=o.get(ne),n=o.get(ee),r=o.get(Xt),s=o.get(Oe),h=o.get(Ve,null,{optional:!0})||o.get(Fe).createRenderer(null,null),m=new Pe(i),g=o.get(De,null,{optional:!0})?.usePopover??!0;m.direction=m.direction||s.value,"showPopover"in t.body?m.usePopover=i?.usePopover??g:m.usePopover=!1;let f=t.createElement("div"),u=t.createElement("div");f.id=n.getId("cdk-overlay-"),f.classList.add("cdk-overlay-pane"),u.appendChild(f),m.usePopover&&(u.setAttribute("popover","manual"),u.classList.add("cdk-overlay-popover"));let S=m.usePopover?m.positionStrategy?.getPopoverInsertionPoint?.():null;return Ct(S)?S.after(u):S?.type==="parent"?S.element.appendChild(u):e.getContainerElement().appendChild(u),new Je(new Ke(f,r,o),u,f,m,o.get(X),o.get(Ki),t,o.get(Gt),o.get(Qi),i?.disableAnimations??o.get(jt,null,{optional:!0})==="NoopAnimations",o.get(Ae),h)}var en=(()=>{class o{scrollStrategies=p(qi);_positionBuilder=p(Ji);_injector=p(j);constructor(){}create(e){return Ot(this._injector,e)}position(){return this._positionBuilder}static \u0275fac=function(t){return new(t||o)};static \u0275prov=G({token:o,factory:o.\u0275fac,providedIn:"root"})}return o})(),un=[{originX:"start",originY:"bottom",overlayX:"start",overlayY:"top"},{originX:"start",originY:"top",overlayX:"start",overlayY:"bottom"},{originX:"end",originY:"top",overlayX:"end",overlayY:"bottom"},{originX:"end",originY:"bottom",overlayX:"end",overlayY:"top"}],fn=new R("cdk-connected-overlay-scroll-strategy",{providedIn:"root",factory:()=>{let o=p(j);return()=>Re(o)}}),_e=(()=>{class o{elementRef=p(D);constructor(){}static \u0275fac=function(t){return new(t||o)};static \u0275dir=ke({type:o,selectors:[["","cdk-overlay-origin",""],["","overlay-origin",""],["","cdkOverlayOrigin",""]],exportAs:["cdkOverlayOrigin"]})}return o})(),tn=new R("cdk-connected-overlay-default-config"),it=(()=>{class o{_dir=p(Oe,{optional:!0});_injector=p(j);_overlayRef;_templatePortal;_backdropSubscription=ie.EMPTY;_attachSubscription=ie.EMPTY;_detachSubscription=ie.EMPTY;_positionSubscription=ie.EMPTY;_offsetX;_offsetY;_position;_scrollStrategyFactory=p(fn);_ngZone=p(X);origin;positions;positionStrategy;get offsetX(){return this._offsetX}set offsetX(e){this._offsetX=e,this._position&&this._updatePositionStrategy(this._position)}get offsetY(){return this._offsetY}set offsetY(e){this._offsetY=e,this._position&&this._updatePositionStrategy(this._position)}width;height;minWidth;minHeight;backdropClass;panelClass;viewportMargin=0;scrollStrategy;open=!1;disableClose=!1;transformOriginSelector;hasBackdrop=!1;lockPosition=!1;flexibleDimensions=!1;growAfterOpen=!1;push=!1;disposeOnNavigation=!1;usePopover;matchWidth=!1;set _config(e){typeof e!="string"&&this._assignConfig(e)}backdropClick=new w;positionChange=new w;attach=new w;detach=new w;overlayKeydown=new w;overlayOutsideClick=new w;constructor(){let e=p(ht),t=p(mt),n=p(tn,{optional:!0}),r=p(De,{optional:!0});this.usePopover=r?.usePopover===!1?null:"global",this._templatePortal=new Ee(e,t),this.scrollStrategy=this._scrollStrategyFactory(),n&&this._assignConfig(n)}get overlayRef(){return this._overlayRef}get dir(){return this._dir?this._dir.value:"ltr"}ngOnDestroy(){this._attachSubscription.unsubscribe(),this._detachSubscription.unsubscribe(),this._backdropSubscription.unsubscribe(),this._positionSubscription.unsubscribe(),this._overlayRef?.dispose()}ngOnChanges(e){this._position&&(this._updatePositionStrategy(this._position),this._overlayRef?.updateSize({width:this._getWidth(),minWidth:this.minWidth,height:this.height,minHeight:this.minHeight}),e.origin&&this.open&&this._position.apply()),e.open&&(this.open?this.attachOverlay():this.detachOverlay())}_createOverlay(){(!this.positions||!this.positions.length)&&(this.positions=un);let e=this._overlayRef=Ot(this._injector,this._buildConfig());this._attachSubscription=e.attachments().subscribe(()=>this.attach.emit()),this._detachSubscription=e.detachments().subscribe(()=>this.detach.emit()),e.keydownEvents().subscribe(t=>{this.overlayKeydown.next(t),t.keyCode===27&&!this.disableClose&&!J(t)&&(t.preventDefault(),this.detachOverlay())}),this._overlayRef.outsidePointerEvents().subscribe(t=>{let n=this._getOriginElement(),r=fe(t);(!n||n!==r&&!n.contains(r))&&this.overlayOutsideClick.next(t)})}_buildConfig(){let e=this._position=this.positionStrategy||this._createPositionStrategy(),t=new Pe({direction:this._dir||"ltr",positionStrategy:e,scrollStrategy:this.scrollStrategy,hasBackdrop:this.hasBackdrop,disposeOnNavigation:this.disposeOnNavigation,usePopover:!!this.usePopover});return(this.height||this.height===0)&&(t.height=this.height),(this.minWidth||this.minWidth===0)&&(t.minWidth=this.minWidth),(this.minHeight||this.minHeight===0)&&(t.minHeight=this.minHeight),this.backdropClass&&(t.backdropClass=this.backdropClass),this.panelClass&&(t.panelClass=this.panelClass),t}_updatePositionStrategy(e){let t=this.positions.map(n=>({originX:n.originX,originY:n.originY,overlayX:n.overlayX,overlayY:n.overlayY,offsetX:n.offsetX||this.offsetX,offsetY:n.offsetY||this.offsetY,panelClass:n.panelClass||void 0}));return e.setOrigin(this._getOrigin()).withPositions(t).withFlexibleDimensions(this.flexibleDimensions).withPush(this.push).withGrowAfterOpen(this.growAfterOpen).withViewportMargin(this.viewportMargin).withLockedPosition(this.lockPosition).withTransformOriginOn(this.transformOriginSelector).withPopoverLocation(this.usePopover===null?"global":this.usePopover)}_createPositionStrategy(){let e=wt(this._injector,this._getOrigin());return this._updatePositionStrategy(e),e}_getOrigin(){return this.origin instanceof _e?this.origin.elementRef:this.origin}_getOriginElement(){return this.origin instanceof _e?this.origin.elementRef.nativeElement:this.origin instanceof D?this.origin.nativeElement:typeof Element<"u"&&this.origin instanceof Element?this.origin:null}_getWidth(){return this.width?this.width:this.matchWidth?this._getOriginElement()?.getBoundingClientRect?.().width:void 0}attachOverlay(){this._overlayRef||this._createOverlay();let e=this._overlayRef;e.getConfig().hasBackdrop=this.hasBackdrop,e.updateSize({width:this._getWidth()}),e.hasAttached()||e.attach(this._templatePortal),this.hasBackdrop?this._backdropSubscription=e.backdropClick().subscribe(t=>this.backdropClick.emit(t)):this._backdropSubscription.unsubscribe(),this._positionSubscription.unsubscribe(),this.positionChange.observers.length>0&&(this._positionSubscription=this._position.positionChanges.pipe(Bt(()=>this.positionChange.observers.length>0)).subscribe(t=>{this._ngZone.run(()=>this.positionChange.emit(t)),this.positionChange.observers.length===0&&this._positionSubscription.unsubscribe()})),this.open=!0}detachOverlay(){this._overlayRef?.detach(),this._backdropSubscription.unsubscribe(),this._positionSubscription.unsubscribe(),this.open=!1}_assignConfig(e){this.origin=e.origin??this.origin,this.positions=e.positions??this.positions,this.positionStrategy=e.positionStrategy??this.positionStrategy,this.offsetX=e.offsetX??this.offsetX,this.offsetY=e.offsetY??this.offsetY,this.width=e.width??this.width,this.height=e.height??this.height,this.minWidth=e.minWidth??this.minWidth,this.minHeight=e.minHeight??this.minHeight,this.backdropClass=e.backdropClass??this.backdropClass,this.panelClass=e.panelClass??this.panelClass,this.viewportMargin=e.viewportMargin??this.viewportMargin,this.scrollStrategy=e.scrollStrategy??this.scrollStrategy,this.disableClose=e.disableClose??this.disableClose,this.transformOriginSelector=e.transformOriginSelector??this.transformOriginSelector,this.hasBackdrop=e.hasBackdrop??this.hasBackdrop,this.lockPosition=e.lockPosition??this.lockPosition,this.flexibleDimensions=e.flexibleDimensions??this.flexibleDimensions,this.growAfterOpen=e.growAfterOpen??this.growAfterOpen,this.push=e.push??this.push,this.disposeOnNavigation=e.disposeOnNavigation??this.disposeOnNavigation,this.usePopover=e.usePopover??this.usePopover,this.matchWidth=e.matchWidth??this.matchWidth}static \u0275fac=function(t){return new(t||o)};static \u0275dir=ke({type:o,selectors:[["","cdk-connected-overlay",""],["","connected-overlay",""],["","cdkConnectedOverlay",""]],inputs:{origin:[0,"cdkConnectedOverlayOrigin","origin"],positions:[0,"cdkConnectedOverlayPositions","positions"],positionStrategy:[0,"cdkConnectedOverlayPositionStrategy","positionStrategy"],offsetX:[0,"cdkConnectedOverlayOffsetX","offsetX"],offsetY:[0,"cdkConnectedOverlayOffsetY","offsetY"],width:[0,"cdkConnectedOverlayWidth","width"],height:[0,"cdkConnectedOverlayHeight","height"],minWidth:[0,"cdkConnectedOverlayMinWidth","minWidth"],minHeight:[0,"cdkConnectedOverlayMinHeight","minHeight"],backdropClass:[0,"cdkConnectedOverlayBackdropClass","backdropClass"],panelClass:[0,"cdkConnectedOverlayPanelClass","panelClass"],viewportMargin:[0,"cdkConnectedOverlayViewportMargin","viewportMargin"],scrollStrategy:[0,"cdkConnectedOverlayScrollStrategy","scrollStrategy"],open:[0,"cdkConnectedOverlayOpen","open"],disableClose:[0,"cdkConnectedOverlayDisableClose","disableClose"],transformOriginSelector:[0,"cdkConnectedOverlayTransformOriginOn","transformOriginSelector"],hasBackdrop:[2,"cdkConnectedOverlayHasBackdrop","hasBackdrop",v],lockPosition:[2,"cdkConnectedOverlayLockPosition","lockPosition",v],flexibleDimensions:[2,"cdkConnectedOverlayFlexibleDimensions","flexibleDimensions",v],growAfterOpen:[2,"cdkConnectedOverlayGrowAfterOpen","growAfterOpen",v],push:[2,"cdkConnectedOverlayPush","push",v],disposeOnNavigation:[2,"cdkConnectedOverlayDisposeOnNavigation","disposeOnNavigation",v],usePopover:[0,"cdkConnectedOverlayUsePopover","usePopover"],matchWidth:[2,"cdkConnectedOverlayMatchWidth","matchWidth",v],_config:[0,"cdkConnectedOverlay","_config"]},outputs:{backdropClick:"backdropClick",positionChange:"positionChange",attach:"attach",detach:"detach",overlayKeydown:"overlayKeydown",overlayOutsideClick:"overlayOutsideClick"},exportAs:["cdkConnectedOverlay"],features:[me]})}return o})(),St=(()=>{class o{static \u0275fac=function(t){return new(t||o)};static \u0275mod=Y({type:o});static \u0275inj=B({providers:[en],imports:[$,Fi,gt,gt]})}return o})();var _n=["text"],gn=[[["mat-icon"]],"*"],bn=["mat-icon","*"];function vn(o,i){if(o&1&&b(0,"mat-pseudo-checkbox",1),o&2){let e=y();_("disabled",e.disabled)("state",e.selected?"checked":"unchecked")}}function yn(o,i){if(o&1&&b(0,"mat-pseudo-checkbox",3),o&2){let e=y();_("disabled",e.disabled)}}function kn(o,i){if(o&1&&(c(0,"span",4),l(1),a()),o&2){let e=y();d(),ze("(",e.group.label,")")}}var Mt=new R("MAT_OPTION_PARENT_COMPONENT"),Pt=new R("MatOptgroup");var Et=class{source;isUserInput;constructor(i,e=!1){this.source=i,this.isUserInput=e}},ge=(()=>{class o{_element=p(D);_changeDetectorRef=p(Z);_parent=p(Mt,{optional:!0});group=p(Pt,{optional:!0});_signalDisableRipple=!1;_selected=!1;_active=!1;_mostRecentViewValue="";get multiple(){return this._parent&&this._parent.multiple}get selected(){return this._selected}value;id=p(ee).getId("mat-option-");get disabled(){return this.group&&this.group.disabled||this._disabled()}set disabled(e){this._disabled.set(e)}_disabled=he(!1);get disableRipple(){return this._signalDisableRipple?this._parent.disableRipple():!!this._parent?.disableRipple}get hideSingleSelectionIndicator(){return!!(this._parent&&this._parent.hideSingleSelectionIndicator)}onSelectionChange=new w;_text;_stateChanges=new P;constructor(){let e=p(ae);e.load(We),e.load(Jt),this._signalDisableRipple=!!this._parent&&Ht(this._parent.disableRipple)}get active(){return this._active}get viewValue(){return(this._text?.nativeElement.textContent||"").trim()}select(e=!0){this._selected||(this._selected=!0,this._changeDetectorRef.markForCheck(),e&&this._emitSelectionChangeEvent())}deselect(e=!0){this._selected&&(this._selected=!1,this._changeDetectorRef.markForCheck(),e&&this._emitSelectionChangeEvent())}focus(e,t){let n=this._getHostElement();typeof n.focus=="function"&&n.focus(t)}setActiveStyles(){this._active||(this._active=!0,this._changeDetectorRef.markForCheck())}setInactiveStyles(){this._active&&(this._active=!1,this._changeDetectorRef.markForCheck())}getLabel(){return this.viewValue}_handleKeydown(e){(e.keyCode===13||e.keyCode===32)&&!J(e)&&(this._selectViaInteraction(),e.preventDefault())}_selectViaInteraction(){this.disabled||(this._selected=this.multiple?!this._selected:!0,this._changeDetectorRef.markForCheck(),this._emitSelectionChangeEvent(!0))}_getTabIndex(){return this.disabled?"-1":"0"}_getHostElement(){return this._element.nativeElement}ngAfterViewChecked(){if(this._selected){let e=this.viewValue;e!==this._mostRecentViewValue&&(this._mostRecentViewValue&&this._stateChanges.next(),this._mostRecentViewValue=e)}}ngOnDestroy(){this._stateChanges.complete()}_emitSelectionChangeEvent(e=!1){this.onSelectionChange.emit(new Et(this,e))}static \u0275fac=function(t){return new(t||o)};static \u0275cmp=A({type:o,selectors:[["mat-option"]],viewQuery:function(t,n){if(t&1&&pe(_n,7),t&2){let r;T(r=F())&&(n._text=r.first)}},hostAttrs:["role","option",1,"mat-mdc-option","mdc-list-item"],hostVars:11,hostBindings:function(t,n){t&1&&x("click",function(){return n._selectViaInteraction()})("keydown",function(s){return n._handleKeydown(s)}),t&2&&(Ne("id",n.id),W("aria-selected",n.selected)("aria-disabled",n.disabled.toString()),C("mdc-list-item--selected",n.selected)("mat-mdc-option-multiple",n.multiple)("mat-mdc-option-active",n.active)("mdc-list-item--disabled",n.disabled))},inputs:{value:"value",id:"id",disabled:[2,"disabled","disabled",v]},outputs:{onSelectionChange:"onSelectionChange"},exportAs:["matOption"],ngContentSelectors:bn,decls:8,vars:5,consts:[["text",""],["aria-hidden","true",1,"mat-mdc-option-pseudo-checkbox",3,"disabled","state"],[1,"mdc-list-item__primary-text"],["state","checked","aria-hidden","true","appearance","minimal",1,"mat-mdc-option-pseudo-checkbox",3,"disabled"],[1,"cdk-visually-hidden"],["aria-hidden","true","mat-ripple","",1,"mat-mdc-option-ripple","mat-focus-indicator",3,"matRippleTrigger","matRippleDisabled"]],template:function(t,n){t&1&&(U(gn),oe(0,vn,1,2,"mat-pseudo-checkbox",1),H(1),c(2,"span",2,0),H(4,1),a(),oe(5,yn,1,1,"mat-pseudo-checkbox",3),oe(6,kn,2,1,"span",4),b(7,"div",5)),t&2&&(re(n.multiple?0:-1),d(5),re(!n.multiple&&n.selected&&!n.hideSingleSelectionIndicator?5:-1),d(),re(n.group&&n.group._inert?6:-1),d(),_("matRippleTrigger",n._getHostElement())("matRippleDisabled",n.disabled||n.disableRipple))},dependencies:[_i,Xe],styles:[`.mat-mdc-option {
  -webkit-user-select: none;
  user-select: none;
  -moz-osx-font-smoothing: grayscale;
  -webkit-font-smoothing: antialiased;
  display: flex;
  position: relative;
  align-items: center;
  justify-content: flex-start;
  overflow: hidden;
  min-height: 48px;
  padding: 0 16px;
  cursor: pointer;
  -webkit-tap-highlight-color: transparent;
  color: var(--mat-option-label-text-color, var(--mat-sys-on-surface));
  font-family: var(--mat-option-label-text-font, var(--mat-sys-label-large-font));
  line-height: var(--mat-option-label-text-line-height, var(--mat-sys-label-large-line-height));
  font-size: var(--mat-option-label-text-size, var(--mat-sys-body-large-size));
  letter-spacing: var(--mat-option-label-text-tracking, var(--mat-sys-label-large-tracking));
  font-weight: var(--mat-option-label-text-weight, var(--mat-sys-body-large-weight));
}
.mat-mdc-option:hover:not(.mdc-list-item--disabled) {
  background-color: var(--mat-option-hover-state-layer-color, color-mix(in srgb, var(--mat-sys-on-surface) calc(var(--mat-sys-hover-state-layer-opacity) * 100%), transparent));
}
.mat-mdc-option:focus.mdc-list-item, .mat-mdc-option.mat-mdc-option-active.mdc-list-item {
  background-color: var(--mat-option-focus-state-layer-color, color-mix(in srgb, var(--mat-sys-on-surface) calc(var(--mat-sys-focus-state-layer-opacity) * 100%), transparent));
  outline: 0;
}
.mat-mdc-option.mdc-list-item--selected:not(.mdc-list-item--disabled):not(.mat-mdc-option-active, .mat-mdc-option-multiple, :focus, :hover) {
  background-color: var(--mat-option-selected-state-layer-color, var(--mat-sys-secondary-container));
}
.mat-mdc-option.mdc-list-item--selected:not(.mdc-list-item--disabled):not(.mat-mdc-option-active, .mat-mdc-option-multiple, :focus, :hover) .mdc-list-item__primary-text {
  color: var(--mat-option-selected-state-label-text-color, var(--mat-sys-on-secondary-container));
}
.mat-mdc-option .mat-pseudo-checkbox {
  --mat-pseudo-checkbox-minimal-selected-checkmark-color: var(--mat-option-selected-state-label-text-color, var(--mat-sys-on-secondary-container));
}
.mat-mdc-option.mdc-list-item {
  align-items: center;
  background: transparent;
}
.mat-mdc-option.mdc-list-item--disabled {
  cursor: default;
  pointer-events: none;
}
.mat-mdc-option.mdc-list-item--disabled .mat-mdc-option-pseudo-checkbox, .mat-mdc-option.mdc-list-item--disabled .mdc-list-item__primary-text, .mat-mdc-option.mdc-list-item--disabled > mat-icon {
  opacity: 0.38;
}
.mat-mdc-optgroup .mat-mdc-option:not(.mat-mdc-option-multiple) {
  padding-left: 32px;
}
[dir=rtl] .mat-mdc-optgroup .mat-mdc-option:not(.mat-mdc-option-multiple) {
  padding-left: 16px;
  padding-right: 32px;
}
.mat-mdc-option .mat-icon,
.mat-mdc-option .mat-pseudo-checkbox-full {
  margin-right: 16px;
  flex-shrink: 0;
}
[dir=rtl] .mat-mdc-option .mat-icon,
[dir=rtl] .mat-mdc-option .mat-pseudo-checkbox-full {
  margin-right: 0;
  margin-left: 16px;
}
.mat-mdc-option .mat-pseudo-checkbox-minimal {
  margin-left: 16px;
  flex-shrink: 0;
}
[dir=rtl] .mat-mdc-option .mat-pseudo-checkbox-minimal {
  margin-right: 16px;
  margin-left: 0;
}
.mat-mdc-option .mat-mdc-option-ripple {
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  position: absolute;
  pointer-events: none;
}
.mat-mdc-option .mdc-list-item__primary-text {
  white-space: normal;
  font-size: inherit;
  font-weight: inherit;
  letter-spacing: inherit;
  line-height: inherit;
  font-family: inherit;
  text-decoration: inherit;
  text-transform: inherit;
  margin-right: auto;
}
[dir=rtl] .mat-mdc-option .mdc-list-item__primary-text {
  margin-right: 0;
  margin-left: auto;
}
@media (forced-colors: active) {
  .mat-mdc-option.mdc-list-item--selected:not(:has(.mat-mdc-option-pseudo-checkbox))::after {
    content: "";
    position: absolute;
    top: 50%;
    right: 16px;
    transform: translateY(-50%);
    width: 10px;
    height: 0;
    border-bottom: solid 10px;
    border-radius: 10px;
  }
  [dir=rtl] .mat-mdc-option.mdc-list-item--selected:not(:has(.mat-mdc-option-pseudo-checkbox))::after {
    right: auto;
    left: 16px;
  }
}

.mat-mdc-option-multiple {
  --mat-list-list-item-selected-container-color: var(--mat-list-list-item-container-color, transparent);
}

.mat-mdc-option-active .mat-focus-indicator::before {
  content: "";
}
`],encapsulation:2,changeDetection:0})}return o})();function nn(o,i,e){if(e.length){let t=i.toArray(),n=e.toArray(),r=0;for(let s=0;s<o+1;s++)t[s].group&&t[s].group===n[r]&&r++;return r}return 0}function on(o,i,e,t){return o<e?o:o+i>e+t?Math.max(0,o-t+i):e}var Rt=(()=>{class o{static \u0275fac=function(t){return new(t||o)};static \u0275mod=Y({type:o});static \u0275inj=B({imports:[bi,gi,ge,$]})}return o})();var On=["trigger"],Sn=["panel"],En=[[["mat-select-trigger"]],"*"],Mn=["mat-select-trigger","*"];function Pn(o,i){if(o&1&&(c(0,"span",4),l(1),a()),o&2){let e=y();d(),q(e.placeholder)}}function Rn(o,i){o&1&&H(0)}function Dn(o,i){if(o&1&&(c(0,"span",11),l(1),a()),o&2){let e=y(2);d(),q(e.triggerValue)}}function In(o,i){if(o&1&&(c(0,"span",5),oe(1,Rn,1,0)(2,Dn,2,1,"span",11),a()),o&2){let e=y();d(),re(e.customTrigger?1:2)}}function An(o,i){if(o&1){let e=xe();c(0,"div",12,1),x("keydown",function(n){L(e);let r=y();return z(r._handleKeydown(n))}),H(2,1),a()}if(o&2){let e=y();Le(e.panelClass),C("mat-select-panel-animations-enabled",!e._animationsDisabled)("mat-primary",(e._parentFormField==null?null:e._parentFormField.color)==="primary")("mat-accent",(e._parentFormField==null?null:e._parentFormField.color)==="accent")("mat-warn",(e._parentFormField==null?null:e._parentFormField.color)==="warn")("mat-undefined",!(e._parentFormField!=null&&e._parentFormField.color)),W("id",e.id+"-panel")("aria-multiselectable",e.multiple)("aria-label",e.ariaLabel||null)("aria-labelledby",e._getPanelAriaLabelledby())}}var Tn=new R("mat-select-scroll-strategy",{providedIn:"root",factory:()=>{let o=p(j);return()=>Re(o)}}),Fn=new R("MAT_SELECT_CONFIG"),Vn=new R("MatSelectTrigger"),Dt=class{source;value;constructor(i,e){this.source=i,this.value=e}},sn=(()=>{class o{_viewportRuler=p(se);_changeDetectorRef=p(Z);_elementRef=p(D);_dir=p(Oe,{optional:!0});_idGenerator=p(ee);_renderer=p(Ve);_parentFormField=p(Ei,{optional:!0});ngControl=p(ci,{self:!0,optional:!0});_liveAnnouncer=p(ei);_defaultOptions=p(Fn,{optional:!0});_animationsDisabled=He();_popoverLocation;_initialized=new P;_cleanupDetach;options;optionGroups;customTrigger;_positions=[{originX:"start",originY:"bottom",overlayX:"start",overlayY:"top"},{originX:"end",originY:"bottom",overlayX:"end",overlayY:"top"},{originX:"start",originY:"top",overlayX:"start",overlayY:"bottom",panelClass:"mat-mdc-select-panel-above"},{originX:"end",originY:"top",overlayX:"end",overlayY:"bottom",panelClass:"mat-mdc-select-panel-above"}];_scrollOptionIntoView(e){let t=this.options.toArray()[e];if(t){let n=this.panel.nativeElement,r=nn(e,this.options,this.optionGroups),s=t._getHostElement();e===0&&r===1?n.scrollTop=0:n.scrollTop=on(s.offsetTop,s.offsetHeight,n.scrollTop,n.offsetHeight)}}_positioningSettled(){this._scrollOptionIntoView(this._keyManager.activeItemIndex||0)}_getChangeEvent(e){return new Dt(this,e)}_scrollStrategyFactory=p(Tn);_panelOpen=!1;_compareWith=(e,t)=>e===t;_uid=this._idGenerator.getId("mat-select-");_triggerAriaLabelledBy=null;_previousControl;_destroy=new P;_errorStateTracker;stateChanges=new P;disableAutomaticLabeling=!0;userAriaDescribedBy;_selectionModel;_keyManager;_preferredOverlayOrigin;_overlayWidth;_onChange=()=>{};_onTouched=()=>{};_valueId=this._idGenerator.getId("mat-select-value-");_scrollStrategy;_overlayPanelClass=this._defaultOptions?.overlayPanelClass||"";get focused(){return this._focused||this._panelOpen}_focused=!1;controlType="mat-select";trigger;panel;_overlayDir;panelClass;disabled=!1;get disableRipple(){return this._disableRipple()}set disableRipple(e){this._disableRipple.set(e)}_disableRipple=he(!1);tabIndex=0;get hideSingleSelectionIndicator(){return this._hideSingleSelectionIndicator}set hideSingleSelectionIndicator(e){this._hideSingleSelectionIndicator=e,this._syncParentProperties()}_hideSingleSelectionIndicator=this._defaultOptions?.hideSingleSelectionIndicator??!1;get placeholder(){return this._placeholder}set placeholder(e){this._placeholder=e,this.stateChanges.next()}_placeholder;get required(){return this._required??this.ngControl?.control?.hasValidator(V.required)??!1}set required(e){this._required=e,this.stateChanges.next()}_required;get multiple(){return this._multiple}set multiple(e){this._selectionModel,this._multiple=e}_multiple=!1;disableOptionCentering=this._defaultOptions?.disableOptionCentering??!1;get compareWith(){return this._compareWith}set compareWith(e){this._compareWith=e,this._selectionModel&&this._initializeSelection()}get value(){return this._value}set value(e){this._assignValue(e)&&this._onChange(e)}_value;ariaLabel="";ariaLabelledby;get errorStateMatcher(){return this._errorStateTracker.matcher}set errorStateMatcher(e){this._errorStateTracker.matcher=e}typeaheadDebounceInterval;sortComparator;get id(){return this._id}set id(e){this._id=e||this._uid,this.stateChanges.next()}_id;get errorState(){return this._errorStateTracker.errorState}set errorState(e){this._errorStateTracker.errorState=e}panelWidth=this._defaultOptions&&typeof this._defaultOptions.panelWidth<"u"?this._defaultOptions.panelWidth:"auto";canSelectNullableOptions=this._defaultOptions?.canSelectNullableOptions??!1;optionSelectionChanges=Vt(()=>{let e=this.options;return e?e.changes.pipe(st(e),ct(()=>Ie(...e.map(t=>t.onSelectionChange)))):this._initialized.pipe(ct(()=>this.optionSelectionChanges))});openedChange=new w;_openedStream=this.openedChange.pipe(ve(e=>e),at(()=>{}));_closedStream=this.openedChange.pipe(ve(e=>!e),at(()=>{}));selectionChange=new w;valueChange=new w;constructor(){let e=p(Pi),t=p(hi,{optional:!0}),n=p(qe,{optional:!0}),r=p(new je("tabindex"),{optional:!0}),s=p(De,{optional:!0});this.ngControl&&(this.ngControl.valueAccessor=this),this._defaultOptions?.typeaheadDebounceInterval!=null&&(this.typeaheadDebounceInterval=this._defaultOptions.typeaheadDebounceInterval),this._errorStateTracker=new Ri(e,this.ngControl,n,t,this.stateChanges),this._scrollStrategy=this._scrollStrategyFactory(),this.tabIndex=r==null?0:parseInt(r)||0,this._popoverLocation=s?.usePopover===!1?null:"inline",this.id=this.id}ngOnInit(){this._selectionModel=new oi(this.multiple),this.stateChanges.next(),this._viewportRuler.change().pipe(de(this._destroy)).subscribe(()=>{this.panelOpen&&(this._overlayWidth=this._getOverlayWidth(this._preferredOverlayOrigin),this._changeDetectorRef.detectChanges())})}ngAfterContentInit(){this._initialized.next(),this._initialized.complete(),this._initKeyManager(),this._selectionModel.changed.pipe(de(this._destroy)).subscribe(e=>{e.added.forEach(t=>t.select()),e.removed.forEach(t=>t.deselect())}),this.options.changes.pipe(st(null),de(this._destroy)).subscribe(()=>{this._resetOptions(),this._initializeSelection()})}ngDoCheck(){let e=this._getTriggerAriaLabelledby(),t=this.ngControl;if(e!==this._triggerAriaLabelledBy){let n=this._elementRef.nativeElement;this._triggerAriaLabelledBy=e,e?n.setAttribute("aria-labelledby",e):n.removeAttribute("aria-labelledby")}t&&(this._previousControl!==t.control&&(this._previousControl!==void 0&&t.disabled!==null&&t.disabled!==this.disabled&&(this.disabled=t.disabled),this._previousControl=t.control),this.updateErrorState())}ngOnChanges(e){(e.disabled||e.userAriaDescribedBy)&&this.stateChanges.next(),e.typeaheadDebounceInterval&&this._keyManager&&this._keyManager.withTypeAhead(this.typeaheadDebounceInterval),e.panelClass&&this.panelClass instanceof Set&&(this.panelClass=Array.from(this.panelClass))}ngOnDestroy(){this._cleanupDetach?.(),this._keyManager?.destroy(),this._destroy.next(),this._destroy.complete(),this.stateChanges.complete(),this._clearFromModal()}toggle(){this.panelOpen?this.close():this.open()}open(){this._canOpen()&&(this._parentFormField&&(this._preferredOverlayOrigin=this._parentFormField.getConnectedOverlayOrigin()),this._cleanupDetach?.(),this._overlayWidth=this._getOverlayWidth(this._preferredOverlayOrigin),this._applyModalPanelOwnership(),this._panelOpen=!0,this._overlayDir.positionChange.pipe(Nt(1)).subscribe(()=>{this._changeDetectorRef.detectChanges(),this._positioningSettled()}),this._overlayDir.attachOverlay(),this._keyManager.withHorizontalOrientation(null),this._highlightCorrectOption(),this._changeDetectorRef.markForCheck(),this.stateChanges.next(),Promise.resolve().then(()=>this.openedChange.emit(!0)))}_trackedModal=null;_applyModalPanelOwnership(){let e=this._elementRef.nativeElement.closest('body > .cdk-overlay-container [aria-modal="true"]');if(!e)return;let t=`${this.id}-panel`;this._trackedModal&&ft(this._trackedModal,"aria-owns",t),ii(e,"aria-owns",t),this._trackedModal=e}_clearFromModal(){if(!this._trackedModal)return;let e=`${this.id}-panel`;ft(this._trackedModal,"aria-owns",e),this._trackedModal=null}close(){this._panelOpen&&(this._panelOpen=!1,this._exitAndDetach(),this._keyManager.withHorizontalOrientation(this._isRtl()?"rtl":"ltr"),this._changeDetectorRef.markForCheck(),this._onTouched(),this.stateChanges.next(),Promise.resolve().then(()=>this.openedChange.emit(!1)))}_exitAndDetach(){if(this._animationsDisabled||!this.panel){this._detachOverlay();return}this._cleanupDetach?.(),this._cleanupDetach=()=>{t(),clearTimeout(n),this._cleanupDetach=void 0};let e=this.panel.nativeElement,t=this._renderer.listen(e,"animationend",r=>{r.animationName==="_mat-select-exit"&&(this._cleanupDetach?.(),this._detachOverlay())}),n=setTimeout(()=>{this._cleanupDetach?.(),this._detachOverlay()},200);e.classList.add("mat-select-panel-exit")}_detachOverlay(){this._overlayDir.detachOverlay(),this._changeDetectorRef.markForCheck()}writeValue(e){this._assignValue(e)}registerOnChange(e){this._onChange=e}registerOnTouched(e){this._onTouched=e}setDisabledState(e){this.disabled=e,this._changeDetectorRef.markForCheck(),this.stateChanges.next()}get panelOpen(){return this._panelOpen}get selected(){return this.multiple?this._selectionModel?.selected||[]:this._selectionModel?.selected[0]}get triggerValue(){if(this.empty)return"";if(this._multiple){let e=this._selectionModel.selected.map(t=>t.viewValue);return this._isRtl()&&e.reverse(),e.join(", ")}return this._selectionModel.selected[0].viewValue}updateErrorState(){this._errorStateTracker.updateErrorState()}_isRtl(){return this._dir?this._dir.value==="rtl":!1}_handleKeydown(e){this.disabled||(this.panelOpen?this._handleOpenKeydown(e):this._handleClosedKeydown(e))}_handleClosedKeydown(e){let t=e.keyCode,n=t===40||t===38||t===37||t===39,r=t===13||t===32,s=this._keyManager;if(!s.isTyping()&&r&&!J(e)||(this.multiple||e.altKey)&&n)e.preventDefault(),this.open();else if(!this.multiple){let h=this.selected;s.onKeydown(e);let m=this.selected;m&&h!==m&&this._liveAnnouncer.announce(m.viewValue,1e4)}}_handleOpenKeydown(e){let t=this._keyManager,n=e.keyCode,r=n===40||n===38,s=t.isTyping();if(r&&e.altKey)e.preventDefault(),this.close();else if(!s&&(n===13||n===32)&&t.activeItem&&!J(e))e.preventDefault(),t.activeItem._selectViaInteraction();else if(!s&&this._multiple&&n===65&&e.ctrlKey){e.preventDefault();let h=this.options.some(m=>!m.disabled&&!m.selected);this.options.forEach(m=>{m.disabled||(h?m.select():m.deselect())})}else{let h=t.activeItemIndex;t.onKeydown(e),this._multiple&&r&&e.shiftKey&&t.activeItem&&t.activeItemIndex!==h&&t.activeItem._selectViaInteraction()}}_handleOverlayKeydown(e){e.keyCode===27&&!J(e)&&(e.preventDefault(),this.close())}_onFocus(){this.disabled||(this._focused=!0,this.stateChanges.next())}_onBlur(){this._focused=!1,this._keyManager?.cancelTypeahead(),!this.disabled&&!this.panelOpen&&(this._onTouched(),this._changeDetectorRef.markForCheck(),this.stateChanges.next())}get empty(){return!this._selectionModel||this._selectionModel.isEmpty()}_initializeSelection(){Promise.resolve().then(()=>{this.ngControl&&(this._value=this.ngControl.value),this._setSelectionByValue(this._value),this.stateChanges.next()})}_setSelectionByValue(e){if(this.options.forEach(t=>t.setInactiveStyles()),this._selectionModel.clear(),this.multiple&&e)Array.isArray(e),e.forEach(t=>this._selectOptionByValue(t)),this._sortValues();else{let t=this._selectOptionByValue(e);t?this._keyManager.updateActiveItem(t):this.panelOpen||this._keyManager.updateActiveItem(-1)}this._changeDetectorRef.markForCheck()}_selectOptionByValue(e){let t=this.options.find(n=>{if(this._selectionModel.isSelected(n))return!1;try{return(n.value!=null||this.canSelectNullableOptions)&&this._compareWith(n.value,e)}catch{return!1}});return t&&this._selectionModel.select(t),t}_assignValue(e){return e!==this._value||this._multiple&&Array.isArray(e)?(this.options&&this._setSelectionByValue(e),this._value=e,!0):!1}_skipPredicate=e=>this.panelOpen?!1:e.disabled;_getOverlayWidth(e){return this.panelWidth==="auto"?(e instanceof _e?e.elementRef:e||this._elementRef).nativeElement.getBoundingClientRect().width:this.panelWidth===null?"":this.panelWidth}_syncParentProperties(){if(this.options)for(let e of this.options)e._changeDetectorRef.markForCheck()}_initKeyManager(){this._keyManager=new ti(this.options).withTypeAhead(this.typeaheadDebounceInterval).withVerticalOrientation().withHorizontalOrientation(this._isRtl()?"rtl":"ltr").withHomeAndEnd().withPageUpDown().withAllowedModifierKeys(["shiftKey"]).skipPredicate(this._skipPredicate),this._keyManager.tabOut.subscribe(()=>{this.panelOpen&&(!this.multiple&&this._keyManager.activeItem&&this._keyManager.activeItem._selectViaInteraction(),this.focus(),this.close())}),this._keyManager.change.subscribe(()=>{this._panelOpen&&this.panel?this._scrollOptionIntoView(this._keyManager.activeItemIndex||0):!this._panelOpen&&!this.multiple&&this._keyManager.activeItem&&this._keyManager.activeItem._selectViaInteraction()})}_resetOptions(){let e=Ie(this.options.changes,this._destroy);this.optionSelectionChanges.pipe(de(e)).subscribe(t=>{this._onSelect(t.source,t.isUserInput),t.isUserInput&&!this.multiple&&this._panelOpen&&(this.close(),this.focus())}),Ie(...this.options.map(t=>t._stateChanges)).pipe(de(e)).subscribe(()=>{this._changeDetectorRef.detectChanges(),this.stateChanges.next()})}_onSelect(e,t){let n=this._selectionModel.isSelected(e);!this.canSelectNullableOptions&&e.value==null&&!this._multiple?(e.deselect(),this._selectionModel.clear(),this.value!=null&&this._propagateChanges(e.value)):(n!==e.selected&&(e.selected?this._selectionModel.select(e):this._selectionModel.deselect(e)),t&&this._keyManager.setActiveItem(e),this.multiple&&(this._sortValues(),t&&this.focus())),n!==this._selectionModel.isSelected(e)&&this._propagateChanges(),this.stateChanges.next()}_sortValues(){if(this.multiple){let e=this.options.toArray();this._selectionModel.sort((t,n)=>this.sortComparator?this.sortComparator(t,n,e):e.indexOf(t)-e.indexOf(n)),this.stateChanges.next()}}_propagateChanges(e){let t;this.multiple?t=this.selected.map(n=>n.value):t=this.selected?this.selected.value:e,this._value=t,this.valueChange.emit(t),this._onChange(t),this.selectionChange.emit(this._getChangeEvent(t)),this._changeDetectorRef.markForCheck()}_highlightCorrectOption(){if(this._keyManager)if(this.empty){let e=-1;for(let t=0;t<this.options.length;t++)if(!this.options.get(t).disabled){e=t;break}this._keyManager.setActiveItem(e)}else this._keyManager.setActiveItem(this._selectionModel.selected[0])}_canOpen(){return!this._panelOpen&&!this.disabled&&this.options?.length>0&&!!this._overlayDir}focus(e){this._elementRef.nativeElement.focus(e)}_getPanelAriaLabelledby(){if(this.ariaLabel)return null;let e=this._parentFormField?.getLabelId()||null,t=e?e+" ":"";return this.ariaLabelledby?t+this.ariaLabelledby:e}_getAriaActiveDescendant(){return this.panelOpen&&this._keyManager&&this._keyManager.activeItem?this._keyManager.activeItem.id:null}_getTriggerAriaLabelledby(){if(this.ariaLabel)return null;let e=this._parentFormField?.getLabelId()||"";return this.ariaLabelledby&&(e+=" "+this.ariaLabelledby),e||(e=this._valueId),e}get describedByIds(){return this._elementRef.nativeElement.getAttribute("aria-describedby")?.split(" ")||[]}setDescribedByIds(e){let t=this._elementRef.nativeElement;e.length?t.setAttribute("aria-describedby",e.join(" ")):t.removeAttribute("aria-describedby")}onContainerClick(e){let t=fe(e);t&&(t.tagName==="MAT-OPTION"||t.classList.contains("cdk-overlay-backdrop")||t.closest(".mat-mdc-select-panel"))||(this.focus(),this.open())}get shouldLabelFloat(){return this.panelOpen||!this.empty||this.focused&&!!this.placeholder}static \u0275fac=function(t){return new(t||o)};static \u0275cmp=A({type:o,selectors:[["mat-select"]],contentQueries:function(t,n,r){if(t&1&&Wt(r,Vn,5)(r,ge,5)(r,Pt,5),t&2){let s;T(s=F())&&(n.customTrigger=s.first),T(s=F())&&(n.options=s),T(s=F())&&(n.optionGroups=s)}},viewQuery:function(t,n){if(t&1&&pe(On,5)(Sn,5)(it,5),t&2){let r;T(r=F())&&(n.trigger=r.first),T(r=F())&&(n.panel=r.first),T(r=F())&&(n._overlayDir=r.first)}},hostAttrs:["role","combobox","aria-haspopup","listbox",1,"mat-mdc-select"],hostVars:21,hostBindings:function(t,n){t&1&&x("keydown",function(s){return n._handleKeydown(s)})("focus",function(){return n._onFocus()})("blur",function(){return n._onBlur()}),t&2&&(W("id",n.id)("tabindex",n.disabled?-1:n.tabIndex)("aria-controls",n.panelOpen?n.id+"-panel":null)("aria-expanded",n.panelOpen)("aria-label",n.ariaLabel||null)("aria-required",n.required.toString())("aria-disabled",n.disabled.toString())("aria-invalid",n.errorState)("aria-activedescendant",n._getAriaActiveDescendant()),C("mat-mdc-select-disabled",n.disabled)("mat-mdc-select-invalid",n.errorState)("mat-mdc-select-required",n.required)("mat-mdc-select-empty",n.empty)("mat-mdc-select-multiple",n.multiple)("mat-select-open",n.panelOpen))},inputs:{userAriaDescribedBy:[0,"aria-describedby","userAriaDescribedBy"],panelClass:"panelClass",disabled:[2,"disabled","disabled",v],disableRipple:[2,"disableRipple","disableRipple",v],tabIndex:[2,"tabIndex","tabIndex",e=>e==null?0:we(e)],hideSingleSelectionIndicator:[2,"hideSingleSelectionIndicator","hideSingleSelectionIndicator",v],placeholder:"placeholder",required:[2,"required","required",v],multiple:[2,"multiple","multiple",v],disableOptionCentering:[2,"disableOptionCentering","disableOptionCentering",v],compareWith:"compareWith",value:"value",ariaLabel:[0,"aria-label","ariaLabel"],ariaLabelledby:[0,"aria-labelledby","ariaLabelledby"],errorStateMatcher:"errorStateMatcher",typeaheadDebounceInterval:[2,"typeaheadDebounceInterval","typeaheadDebounceInterval",we],sortComparator:"sortComparator",id:"id",panelWidth:"panelWidth",canSelectNullableOptions:[2,"canSelectNullableOptions","canSelectNullableOptions",v]},outputs:{openedChange:"openedChange",_openedStream:"opened",_closedStream:"closed",selectionChange:"selectionChange",valueChange:"valueChange"},exportAs:["matSelect"],features:[Ce([{provide:Si,useExisting:o},{provide:Mt,useExisting:o}]),me],ngContentSelectors:Mn,decls:11,vars:10,consts:[["fallbackOverlayOrigin","cdkOverlayOrigin","trigger",""],["panel",""],["cdk-overlay-origin","",1,"mat-mdc-select-trigger",3,"click"],[1,"mat-mdc-select-value"],[1,"mat-mdc-select-placeholder","mat-mdc-select-min-line"],[1,"mat-mdc-select-value-text"],[1,"mat-mdc-select-arrow-wrapper"],[1,"mat-mdc-select-arrow"],["viewBox","0 0 24 24","width","24px","height","24px","focusable","false","aria-hidden","true"],["d","M7 10l5 5 5-5z"],["cdk-connected-overlay","","cdkConnectedOverlayHasBackdrop","","cdkConnectedOverlayBackdropClass","cdk-overlay-transparent-backdrop",3,"detach","backdropClick","overlayKeydown","cdkConnectedOverlayDisableClose","cdkConnectedOverlayPanelClass","cdkConnectedOverlayScrollStrategy","cdkConnectedOverlayOrigin","cdkConnectedOverlayPositions","cdkConnectedOverlayWidth","cdkConnectedOverlayFlexibleDimensions","cdkConnectedOverlayUsePopover"],[1,"mat-mdc-select-min-line"],["role","listbox","tabindex","-1",1,"mat-mdc-select-panel","mdc-menu-surface","mdc-menu-surface--open",3,"keydown"]],template:function(t,n){if(t&1&&(U(En),c(0,"div",2,0),x("click",function(){return n.open()}),c(3,"div",3),oe(4,Pn,2,1,"span",4)(5,In,3,1,"span",5),a(),c(6,"div",6)(7,"div",7),Te(),c(8,"svg",8),b(9,"path",9),a()()()(),E(10,An,3,16,"ng-template",10),x("detach",function(){return n.close()})("backdropClick",function(){return n.close()})("overlayKeydown",function(s){return n._handleOverlayKeydown(s)})),t&2){let r=Be(1);d(3),W("id",n._valueId),d(),re(n.empty?4:5),d(6),_("cdkConnectedOverlayDisableClose",!0)("cdkConnectedOverlayPanelClass",n._overlayPanelClass)("cdkConnectedOverlayScrollStrategy",n._scrollStrategy)("cdkConnectedOverlayOrigin",n._preferredOverlayOrigin||r)("cdkConnectedOverlayPositions",n._positions)("cdkConnectedOverlayWidth",n._overlayWidth)("cdkConnectedOverlayFlexibleDimensions",!0)("cdkConnectedOverlayUsePopover",n._popoverLocation)}},dependencies:[_e,it],styles:[`@keyframes _mat-select-enter {
  from {
    opacity: 0;
    transform: scaleY(0.8);
  }
  to {
    opacity: 1;
    transform: none;
  }
}
@keyframes _mat-select-exit {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}
.mat-mdc-select {
  display: inline-block;
  width: 100%;
  outline: none;
  -moz-osx-font-smoothing: grayscale;
  -webkit-font-smoothing: antialiased;
  color: var(--mat-select-enabled-trigger-text-color, var(--mat-sys-on-surface));
  font-family: var(--mat-select-trigger-text-font, var(--mat-sys-body-large-font));
  line-height: var(--mat-select-trigger-text-line-height, var(--mat-sys-body-large-line-height));
  font-size: var(--mat-select-trigger-text-size, var(--mat-sys-body-large-size));
  font-weight: var(--mat-select-trigger-text-weight, var(--mat-sys-body-large-weight));
  letter-spacing: var(--mat-select-trigger-text-tracking, var(--mat-sys-body-large-tracking));
}

div.mat-mdc-select-panel {
  box-shadow: var(--mat-select-container-elevation-shadow, 0px 3px 1px -2px rgba(0, 0, 0, 0.2), 0px 2px 2px 0px rgba(0, 0, 0, 0.14), 0px 1px 5px 0px rgba(0, 0, 0, 0.12));
}

.mat-mdc-select-disabled {
  color: var(--mat-select-disabled-trigger-text-color, color-mix(in srgb, var(--mat-sys-on-surface) 38%, transparent));
}
.mat-mdc-select-disabled .mat-mdc-select-placeholder {
  color: var(--mat-select-disabled-trigger-text-color, color-mix(in srgb, var(--mat-sys-on-surface) 38%, transparent));
}

.mat-mdc-select-trigger {
  display: inline-flex;
  align-items: center;
  cursor: pointer;
  position: relative;
  box-sizing: border-box;
  width: 100%;
}
.mat-mdc-select-disabled .mat-mdc-select-trigger {
  -webkit-user-select: none;
  user-select: none;
  cursor: default;
}

.mat-mdc-select-value {
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.mat-mdc-select-value-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.mat-mdc-select-arrow-wrapper {
  height: 24px;
  flex-shrink: 0;
  display: inline-flex;
  align-items: center;
}
.mat-form-field-appearance-fill .mdc-text-field--no-label .mat-mdc-select-arrow-wrapper {
  transform: none;
}

.mat-mdc-form-field .mat-mdc-select.mat-mdc-select-invalid .mat-mdc-select-arrow,
.mat-form-field-invalid:not(.mat-form-field-disabled) .mat-mdc-form-field-infix::after {
  color: var(--mat-select-invalid-arrow-color, var(--mat-sys-error));
}

.mat-mdc-select-arrow {
  width: 10px;
  height: 5px;
  position: relative;
  color: var(--mat-select-enabled-arrow-color, var(--mat-sys-on-surface-variant));
}
.mat-mdc-form-field.mat-focused .mat-mdc-select-arrow {
  color: var(--mat-select-focused-arrow-color, var(--mat-sys-primary));
}
.mat-mdc-form-field .mat-mdc-select.mat-mdc-select-disabled .mat-mdc-select-arrow {
  color: var(--mat-select-disabled-arrow-color, color-mix(in srgb, var(--mat-sys-on-surface) 38%, transparent));
}
.mat-select-open .mat-mdc-select-arrow {
  transform: rotate(180deg);
}
.mat-form-field-animations-enabled .mat-mdc-select-arrow {
  transition: transform 80ms linear;
}
.mat-mdc-select-arrow svg {
  fill: currentColor;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
@media (forced-colors: active) {
  .mat-mdc-select-arrow svg {
    fill: CanvasText;
  }
  .mat-mdc-select-disabled .mat-mdc-select-arrow svg {
    fill: GrayText;
  }
}

div.mat-mdc-select-panel {
  width: 100%;
  max-height: 275px;
  outline: 0;
  overflow: auto;
  padding: 8px 0;
  box-sizing: border-box;
  transform-origin: top center;
  border-radius: 0 0 4px 4px;
  position: relative;
  background-color: var(--mat-select-panel-background-color, var(--mat-sys-surface-container));
}
.mat-mdc-select-panel-above div.mat-mdc-select-panel {
  border-radius: 4px 4px 0 0;
  transform-origin: bottom center;
}
@media (forced-colors: active) {
  div.mat-mdc-select-panel {
    outline: solid 1px;
  }
}

.mat-select-panel-animations-enabled {
  animation: _mat-select-enter 120ms cubic-bezier(0, 0, 0.2, 1);
}
.mat-select-panel-animations-enabled.mat-select-panel-exit {
  animation: _mat-select-exit 100ms linear;
}

.mat-mdc-select-placeholder {
  transition: color 400ms 133.3333333333ms cubic-bezier(0.25, 0.8, 0.25, 1);
  color: var(--mat-select-placeholder-text-color, var(--mat-sys-on-surface-variant));
}
.mat-mdc-form-field:not(.mat-form-field-animations-enabled) .mat-mdc-select-placeholder, ._mat-animation-noopable .mat-mdc-select-placeholder {
  transition: none;
}
.mat-form-field-hide-placeholder .mat-mdc-select-placeholder {
  color: transparent;
  -webkit-text-fill-color: transparent;
  transition: none;
  display: block;
}

.mat-mdc-form-field-type-mat-select:not(.mat-form-field-disabled) .mat-mdc-text-field-wrapper {
  cursor: pointer;
}
.mat-mdc-form-field-type-mat-select.mat-form-field-appearance-fill .mat-mdc-floating-label {
  max-width: calc(100% - 18px);
}
.mat-mdc-form-field-type-mat-select.mat-form-field-appearance-fill .mdc-floating-label--float-above {
  max-width: calc(100% / 0.75 - 24px);
}
.mat-mdc-form-field-type-mat-select.mat-form-field-appearance-outline .mdc-notched-outline__notch {
  max-width: calc(100% - 60px);
}
.mat-mdc-form-field-type-mat-select.mat-form-field-appearance-outline .mdc-text-field--label-floating .mdc-notched-outline__notch {
  max-width: calc(100% - 24px);
}

.mat-mdc-select-min-line:empty::before {
  content: " ";
  white-space: pre;
  width: 1px;
  display: inline-block;
  visibility: hidden;
}

.mat-form-field-appearance-fill .mat-mdc-select-arrow-wrapper {
  transform: var(--mat-select-arrow-transform, translateY(-8px));
}
`],encapsulation:2,changeDetection:0})}return o})();var cn=(()=>{class o{static \u0275fac=function(t){return new(t||o)};static \u0275mod=Y({type:o});static \u0275inj=B({imports:[St,Rt,$,ni,Ge,Rt]})}return o})();var Bn=["mat-internal-form-field",""],Ln=["*"],ln=(()=>{class o{labelPosition="after";static \u0275fac=function(t){return new(t||o)};static \u0275cmp=A({type:o,selectors:[["div","mat-internal-form-field",""]],hostAttrs:[1,"mdc-form-field","mat-internal-form-field"],hostVars:2,hostBindings:function(t,n){t&2&&C("mdc-form-field--align-end",n.labelPosition==="before")},inputs:{labelPosition:"labelPosition"},attrs:Bn,ngContentSelectors:Ln,decls:1,vars:0,template:function(t,n){t&1&&(U(),H(0))},styles:[`.mat-internal-form-field {
  -moz-osx-font-smoothing: grayscale;
  -webkit-font-smoothing: antialiased;
  display: inline-flex;
  align-items: center;
  vertical-align: middle;
}
.mat-internal-form-field > label {
  margin-left: 0;
  margin-right: auto;
  padding-left: 4px;
  padding-right: 0;
  order: 0;
}
[dir=rtl] .mat-internal-form-field > label {
  margin-left: auto;
  margin-right: 0;
  padding-left: 0;
  padding-right: 4px;
}

.mdc-form-field--align-end > label {
  margin-left: auto;
  margin-right: 0;
  padding-left: 0;
  padding-right: 4px;
  order: -1;
}
[dir=rtl] .mdc-form-field--align-end .mdc-form-field--align-end label {
  margin-left: 0;
  margin-right: auto;
  padding-left: 4px;
  padding-right: 0;
}
`],encapsulation:2,changeDetection:0})}return o})();var zn=["input"],jn=["label"],Yn=["*"],It={color:"accent",clickAction:"check-indeterminate",disabledInteractive:!1},Hn=new R("mat-checkbox-default-options",{providedIn:"root",factory:()=>It}),O=(function(o){return o[o.Init=0]="Init",o[o.Checked=1]="Checked",o[o.Unchecked=2]="Unchecked",o[o.Indeterminate=3]="Indeterminate",o})(O||{}),At=class{source;checked},Tt=(()=>{class o{_elementRef=p(D);_changeDetectorRef=p(Z);_ngZone=p(X);_animationsDisabled=He();_options=p(Hn,{optional:!0});focus(){this._inputElement.nativeElement.focus()}_createChangeEvent(e){let t=new At;return t.source=this,t.checked=e,t}_getAnimationTargetElement(){return this._inputElement?.nativeElement}_animationClasses={uncheckedToChecked:"mdc-checkbox--anim-unchecked-checked",uncheckedToIndeterminate:"mdc-checkbox--anim-unchecked-indeterminate",checkedToUnchecked:"mdc-checkbox--anim-checked-unchecked",checkedToIndeterminate:"mdc-checkbox--anim-checked-indeterminate",indeterminateToChecked:"mdc-checkbox--anim-indeterminate-checked",indeterminateToUnchecked:"mdc-checkbox--anim-indeterminate-unchecked"};ariaLabel="";ariaLabelledby=null;ariaDescribedby;ariaExpanded;ariaControls;ariaOwns;_uniqueId;id;get inputId(){return`${this.id||this._uniqueId}-input`}required=!1;labelPosition="after";name=null;change=new w;indeterminateChange=new w;value;disableRipple=!1;_inputElement;_labelElement;tabIndex;color;disabledInteractive;_onTouched=()=>{};_currentAnimationClass="";_currentCheckState=O.Init;_controlValueAccessorChangeFn=()=>{};_validatorChangeFn=()=>{};constructor(){p(ae).load(We);let e=p(new je("tabindex"),{optional:!0});this._options=this._options||It,this.color=this._options.color||It.color,this.tabIndex=e==null?0:parseInt(e)||0,this.id=this._uniqueId=p(ee).getId("mat-mdc-checkbox-"),this.disabledInteractive=this._options?.disabledInteractive??!1}ngOnChanges(e){e.required&&this._validatorChangeFn()}ngAfterViewInit(){this._syncIndeterminate(this.indeterminate)}get checked(){return this._checked}set checked(e){e!=this.checked&&(this._checked=e,this._changeDetectorRef.markForCheck())}_checked=!1;get disabled(){return this._disabled}set disabled(e){e!==this.disabled&&(this._disabled=e,this._changeDetectorRef.markForCheck())}_disabled=!1;get indeterminate(){return this._indeterminate()}set indeterminate(e){let t=e!=this._indeterminate();this._indeterminate.set(e),t&&(e?this._transitionCheckState(O.Indeterminate):this._transitionCheckState(this.checked?O.Checked:O.Unchecked),this.indeterminateChange.emit(e)),this._syncIndeterminate(e)}_indeterminate=he(!1);_isRippleDisabled(){return this.disableRipple||this.disabled}_onLabelTextChange(){this._changeDetectorRef.detectChanges()}writeValue(e){this.checked=!!e}registerOnChange(e){this._controlValueAccessorChangeFn=e}registerOnTouched(e){this._onTouched=e}setDisabledState(e){this.disabled=e}validate(e){return this.required&&e.value!==!0?{required:!0}:null}registerOnValidatorChange(e){this._validatorChangeFn=e}_transitionCheckState(e){let t=this._currentCheckState,n=this._getAnimationTargetElement();if(!(t===e||!n)&&(this._currentAnimationClass&&n.classList.remove(this._currentAnimationClass),this._currentAnimationClass=this._getAnimationClassForCheckStateTransition(t,e),this._currentCheckState=e,this._currentAnimationClass.length>0)){n.classList.add(this._currentAnimationClass);let r=this._currentAnimationClass;this._ngZone.runOutsideAngular(()=>{setTimeout(()=>{n.classList.remove(r)},1e3)})}}_emitChangeEvent(){this._controlValueAccessorChangeFn(this.checked),this.change.emit(this._createChangeEvent(this.checked)),this._inputElement&&(this._inputElement.nativeElement.checked=this.checked)}toggle(){this.checked=!this.checked,this._controlValueAccessorChangeFn(this.checked)}_handleInputClick(){let e=this._options?.clickAction;!this.disabled&&e!=="noop"?(this.indeterminate&&e!=="check"&&Promise.resolve().then(()=>{this._indeterminate.set(!1),this.indeterminateChange.emit(!1)}),this._checked=!this._checked,this._transitionCheckState(this._checked?O.Checked:O.Unchecked),this._emitChangeEvent()):(this.disabled&&this.disabledInteractive||!this.disabled&&e==="noop")&&(this._inputElement.nativeElement.checked=this.checked,this._inputElement.nativeElement.indeterminate=this.indeterminate)}_onInteractionEvent(e){e.stopPropagation()}_onBlur(){Promise.resolve().then(()=>{this._onTouched(),this._changeDetectorRef.markForCheck()})}_getAnimationClassForCheckStateTransition(e,t){if(this._animationsDisabled)return"";switch(e){case O.Init:if(t===O.Checked)return this._animationClasses.uncheckedToChecked;if(t==O.Indeterminate)return this._checked?this._animationClasses.checkedToIndeterminate:this._animationClasses.uncheckedToIndeterminate;break;case O.Unchecked:return t===O.Checked?this._animationClasses.uncheckedToChecked:this._animationClasses.uncheckedToIndeterminate;case O.Checked:return t===O.Unchecked?this._animationClasses.checkedToUnchecked:this._animationClasses.checkedToIndeterminate;case O.Indeterminate:return t===O.Checked?this._animationClasses.indeterminateToChecked:this._animationClasses.indeterminateToUnchecked}return""}_syncIndeterminate(e){let t=this._inputElement;t&&(t.nativeElement.indeterminate=e)}_onInputClick(){this._handleInputClick()}_onTouchTargetClick(){this._handleInputClick(),this.disabled||this._inputElement.nativeElement.focus()}_preventBubblingFromLabel(e){e.target&&this._labelElement.nativeElement.contains(e.target)&&e.stopPropagation()}static \u0275fac=function(t){return new(t||o)};static \u0275cmp=A({type:o,selectors:[["mat-checkbox"]],viewQuery:function(t,n){if(t&1&&pe(zn,5)(jn,5),t&2){let r;T(r=F())&&(n._inputElement=r.first),T(r=F())&&(n._labelElement=r.first)}},hostAttrs:[1,"mat-mdc-checkbox"],hostVars:16,hostBindings:function(t,n){t&2&&(Ne("id",n.id),W("tabindex",null)("aria-label",null)("aria-labelledby",null),Le(n.color?"mat-"+n.color:"mat-accent"),C("_mat-animation-noopable",n._animationsDisabled)("mdc-checkbox--disabled",n.disabled)("mat-mdc-checkbox-disabled",n.disabled)("mat-mdc-checkbox-checked",n.checked)("mat-mdc-checkbox-disabled-interactive",n.disabledInteractive))},inputs:{ariaLabel:[0,"aria-label","ariaLabel"],ariaLabelledby:[0,"aria-labelledby","ariaLabelledby"],ariaDescribedby:[0,"aria-describedby","ariaDescribedby"],ariaExpanded:[2,"aria-expanded","ariaExpanded",v],ariaControls:[0,"aria-controls","ariaControls"],ariaOwns:[0,"aria-owns","ariaOwns"],id:"id",required:[2,"required","required",v],labelPosition:"labelPosition",name:"name",value:"value",disableRipple:[2,"disableRipple","disableRipple",v],tabIndex:[2,"tabIndex","tabIndex",e=>e==null?void 0:we(e)],color:"color",disabledInteractive:[2,"disabledInteractive","disabledInteractive",v],checked:[2,"checked","checked",v],disabled:[2,"disabled","disabled",v],indeterminate:[2,"indeterminate","indeterminate",v]},outputs:{change:"change",indeterminateChange:"indeterminateChange"},exportAs:["matCheckbox"],features:[Ce([{provide:ri,useExisting:Lt(()=>o),multi:!0},{provide:si,useExisting:o,multi:!0}]),me],ngContentSelectors:Yn,decls:15,vars:23,consts:[["checkbox",""],["input",""],["label",""],["mat-internal-form-field","",3,"click","labelPosition"],[1,"mdc-checkbox"],["aria-hidden","true",1,"mat-mdc-checkbox-touch-target",3,"click"],["type","checkbox",1,"mdc-checkbox__native-control",3,"blur","click","change","checked","indeterminate","disabled","id","required","tabIndex"],["aria-hidden","true",1,"mdc-checkbox__ripple"],["aria-hidden","true",1,"mdc-checkbox__background"],["focusable","false","viewBox","0 0 24 24",1,"mdc-checkbox__checkmark"],["fill","none","d","M1.73,12.91 8.1,19.28 22.79,4.59",1,"mdc-checkbox__checkmark-path"],[1,"mdc-checkbox__mixedmark"],["mat-ripple","","aria-hidden","true",1,"mat-mdc-checkbox-ripple","mat-focus-indicator",3,"matRippleTrigger","matRippleDisabled","matRippleCentered"],[1,"mdc-label",3,"for"]],template:function(t,n){if(t&1&&(U(),c(0,"div",3),x("click",function(s){return n._preventBubblingFromLabel(s)}),c(1,"div",4,0)(3,"div",5),x("click",function(){return n._onTouchTargetClick()}),a(),c(4,"input",6,1),x("blur",function(){return n._onBlur()})("click",function(){return n._onInputClick()})("change",function(s){return n._onInteractionEvent(s)}),a(),b(6,"div",7),c(7,"div",8),Te(),c(8,"svg",9),b(9,"path",10),a(),zt(),b(10,"div",11),a(),b(11,"div",12),a(),c(12,"label",13,2),H(14),a()()),t&2){let r=Be(2);_("labelPosition",n.labelPosition),d(4),C("mdc-checkbox--selected",n.checked),_("checked",n.checked)("indeterminate",n.indeterminate)("disabled",n.disabled&&!n.disabledInteractive)("id",n.inputId)("required",n.required)("tabIndex",n.disabled&&!n.disabledInteractive?-1:n.tabIndex),W("aria-label",n.ariaLabel||null)("aria-labelledby",n.ariaLabelledby)("aria-describedby",n.ariaDescribedby)("aria-checked",n.indeterminate?"mixed":null)("aria-controls",n.ariaControls)("aria-disabled",n.disabled&&n.disabledInteractive?!0:null)("aria-expanded",n.ariaExpanded)("aria-owns",n.ariaOwns)("name",n.name)("value",n.value),d(7),_("matRippleTrigger",r)("matRippleDisabled",n.disableRipple||n.disabled)("matRippleCentered",!0),d(),_("for",n.inputId)}},dependencies:[Xe,ln],styles:[`.mdc-checkbox {
  display: inline-block;
  position: relative;
  flex: 0 0 18px;
  box-sizing: content-box;
  width: 18px;
  height: 18px;
  line-height: 0;
  white-space: nowrap;
  cursor: pointer;
  vertical-align: bottom;
  padding: calc((var(--mat-checkbox-state-layer-size, 40px) - 18px) / 2);
  margin: calc((var(--mat-checkbox-state-layer-size, 40px) - var(--mat-checkbox-state-layer-size, 40px)) / 2);
}
.mdc-checkbox:hover > .mdc-checkbox__ripple {
  opacity: var(--mat-checkbox-unselected-hover-state-layer-opacity, var(--mat-sys-hover-state-layer-opacity));
  background-color: var(--mat-checkbox-unselected-hover-state-layer-color, var(--mat-sys-on-surface));
}
.mdc-checkbox:hover > .mat-mdc-checkbox-ripple > .mat-ripple-element {
  background-color: var(--mat-checkbox-unselected-hover-state-layer-color, var(--mat-sys-on-surface));
}
.mdc-checkbox .mdc-checkbox__native-control:focus + .mdc-checkbox__ripple {
  opacity: var(--mat-checkbox-unselected-focus-state-layer-opacity, var(--mat-sys-focus-state-layer-opacity));
  background-color: var(--mat-checkbox-unselected-focus-state-layer-color, var(--mat-sys-on-surface));
}
.mdc-checkbox .mdc-checkbox__native-control:focus ~ .mat-mdc-checkbox-ripple .mat-ripple-element {
  background-color: var(--mat-checkbox-unselected-focus-state-layer-color, var(--mat-sys-on-surface));
}
.mdc-checkbox:active > .mdc-checkbox__native-control + .mdc-checkbox__ripple {
  opacity: var(--mat-checkbox-unselected-pressed-state-layer-opacity, var(--mat-sys-pressed-state-layer-opacity));
  background-color: var(--mat-checkbox-unselected-pressed-state-layer-color, var(--mat-sys-primary));
}
.mdc-checkbox:active > .mdc-checkbox__native-control ~ .mat-mdc-checkbox-ripple .mat-ripple-element {
  background-color: var(--mat-checkbox-unselected-pressed-state-layer-color, var(--mat-sys-primary));
}
.mdc-checkbox:hover > .mdc-checkbox__native-control:checked + .mdc-checkbox__ripple {
  opacity: var(--mat-checkbox-selected-hover-state-layer-opacity, var(--mat-sys-hover-state-layer-opacity));
  background-color: var(--mat-checkbox-selected-hover-state-layer-color, var(--mat-sys-primary));
}
.mdc-checkbox:hover > .mdc-checkbox__native-control:checked ~ .mat-mdc-checkbox-ripple .mat-ripple-element {
  background-color: var(--mat-checkbox-selected-hover-state-layer-color, var(--mat-sys-primary));
}
.mdc-checkbox .mdc-checkbox__native-control:focus:checked + .mdc-checkbox__ripple {
  opacity: var(--mat-checkbox-selected-focus-state-layer-opacity, var(--mat-sys-focus-state-layer-opacity));
  background-color: var(--mat-checkbox-selected-focus-state-layer-color, var(--mat-sys-primary));
}
.mdc-checkbox .mdc-checkbox__native-control:focus:checked ~ .mat-mdc-checkbox-ripple .mat-ripple-element {
  background-color: var(--mat-checkbox-selected-focus-state-layer-color, var(--mat-sys-primary));
}
.mdc-checkbox:active > .mdc-checkbox__native-control:checked + .mdc-checkbox__ripple {
  opacity: var(--mat-checkbox-selected-pressed-state-layer-opacity, var(--mat-sys-pressed-state-layer-opacity));
  background-color: var(--mat-checkbox-selected-pressed-state-layer-color, var(--mat-sys-on-surface));
}
.mdc-checkbox:active > .mdc-checkbox__native-control:checked ~ .mat-mdc-checkbox-ripple .mat-ripple-element {
  background-color: var(--mat-checkbox-selected-pressed-state-layer-color, var(--mat-sys-on-surface));
}
.mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox .mdc-checkbox__native-control ~ .mat-mdc-checkbox-ripple .mat-ripple-element,
.mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox .mdc-checkbox__native-control + .mdc-checkbox__ripple {
  background-color: var(--mat-checkbox-unselected-hover-state-layer-color, var(--mat-sys-on-surface));
}
.mdc-checkbox .mdc-checkbox__native-control {
  position: absolute;
  margin: 0;
  padding: 0;
  opacity: 0;
  cursor: inherit;
  z-index: 1;
  width: var(--mat-checkbox-state-layer-size, 40px);
  height: var(--mat-checkbox-state-layer-size, 40px);
  top: calc((var(--mat-checkbox-state-layer-size, 40px) - var(--mat-checkbox-state-layer-size, 40px)) / 2);
  right: calc((var(--mat-checkbox-state-layer-size, 40px) - var(--mat-checkbox-state-layer-size, 40px)) / 2);
  left: calc((var(--mat-checkbox-state-layer-size, 40px) - var(--mat-checkbox-state-layer-size, 40px)) / 2);
}

.mdc-checkbox--disabled {
  cursor: default;
  pointer-events: none;
}

.mdc-checkbox__background {
  display: inline-flex;
  position: absolute;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  width: 18px;
  height: 18px;
  border: 2px solid currentColor;
  border-radius: 2px;
  background-color: transparent;
  pointer-events: none;
  will-change: background-color, border-color;
  transition: background-color 90ms cubic-bezier(0.4, 0, 0.6, 1), border-color 90ms cubic-bezier(0.4, 0, 0.6, 1);
  -webkit-print-color-adjust: exact;
  color-adjust: exact;
  border-color: var(--mat-checkbox-unselected-icon-color, var(--mat-sys-on-surface-variant));
  top: calc((var(--mat-checkbox-state-layer-size, 40px) - 18px) / 2);
  left: calc((var(--mat-checkbox-state-layer-size, 40px) - 18px) / 2);
}

.mdc-checkbox__native-control:enabled:checked ~ .mdc-checkbox__background,
.mdc-checkbox__native-control:enabled:indeterminate ~ .mdc-checkbox__background {
  border-color: var(--mat-checkbox-selected-icon-color, var(--mat-sys-primary));
  background-color: var(--mat-checkbox-selected-icon-color, var(--mat-sys-primary));
}

.mdc-checkbox--disabled .mdc-checkbox__background {
  border-color: var(--mat-checkbox-disabled-unselected-icon-color, color-mix(in srgb, var(--mat-sys-on-surface) 38%, transparent));
}
@media (forced-colors: active) {
  .mdc-checkbox--disabled .mdc-checkbox__background {
    border-color: GrayText;
  }
}

.mdc-checkbox__native-control:disabled:checked ~ .mdc-checkbox__background,
.mdc-checkbox__native-control:disabled:indeterminate ~ .mdc-checkbox__background {
  background-color: var(--mat-checkbox-disabled-selected-icon-color, color-mix(in srgb, var(--mat-sys-on-surface) 38%, transparent));
  border-color: transparent;
}
@media (forced-colors: active) {
  .mdc-checkbox__native-control:disabled:checked ~ .mdc-checkbox__background,
  .mdc-checkbox__native-control:disabled:indeterminate ~ .mdc-checkbox__background {
    border-color: GrayText;
  }
}

.mdc-checkbox:hover > .mdc-checkbox__native-control:not(:checked) ~ .mdc-checkbox__background,
.mdc-checkbox:hover > .mdc-checkbox__native-control:not(:indeterminate) ~ .mdc-checkbox__background {
  border-color: var(--mat-checkbox-unselected-hover-icon-color, var(--mat-sys-on-surface));
  background-color: transparent;
}

.mdc-checkbox:hover > .mdc-checkbox__native-control:checked ~ .mdc-checkbox__background,
.mdc-checkbox:hover > .mdc-checkbox__native-control:indeterminate ~ .mdc-checkbox__background {
  border-color: var(--mat-checkbox-selected-hover-icon-color, var(--mat-sys-primary));
  background-color: var(--mat-checkbox-selected-hover-icon-color, var(--mat-sys-primary));
}

.mdc-checkbox__native-control:focus:focus:not(:checked) ~ .mdc-checkbox__background,
.mdc-checkbox__native-control:focus:focus:not(:indeterminate) ~ .mdc-checkbox__background {
  border-color: var(--mat-checkbox-unselected-focus-icon-color, var(--mat-sys-on-surface));
}

.mdc-checkbox__native-control:focus:focus:checked ~ .mdc-checkbox__background,
.mdc-checkbox__native-control:focus:focus:indeterminate ~ .mdc-checkbox__background {
  border-color: var(--mat-checkbox-selected-focus-icon-color, var(--mat-sys-primary));
  background-color: var(--mat-checkbox-selected-focus-icon-color, var(--mat-sys-primary));
}

.mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox:hover > .mdc-checkbox__native-control ~ .mdc-checkbox__background,
.mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox .mdc-checkbox__native-control:focus ~ .mdc-checkbox__background,
.mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox__background {
  border-color: var(--mat-checkbox-disabled-unselected-icon-color, color-mix(in srgb, var(--mat-sys-on-surface) 38%, transparent));
}
@media (forced-colors: active) {
  .mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox:hover > .mdc-checkbox__native-control ~ .mdc-checkbox__background,
  .mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox .mdc-checkbox__native-control:focus ~ .mdc-checkbox__background,
  .mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox__background {
    border-color: GrayText;
  }
}
.mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox__native-control:checked ~ .mdc-checkbox__background,
.mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox__native-control:indeterminate ~ .mdc-checkbox__background {
  background-color: var(--mat-checkbox-disabled-selected-icon-color, color-mix(in srgb, var(--mat-sys-on-surface) 38%, transparent));
  border-color: transparent;
}

.mdc-checkbox__checkmark {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  width: 100%;
  opacity: 0;
  transition: opacity 180ms cubic-bezier(0.4, 0, 0.6, 1);
  color: var(--mat-checkbox-selected-checkmark-color, var(--mat-sys-on-primary));
}
@media (forced-colors: active) {
  .mdc-checkbox__checkmark {
    color: CanvasText;
  }
}

.mdc-checkbox--disabled .mdc-checkbox__checkmark, .mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox__checkmark {
  color: var(--mat-checkbox-disabled-selected-checkmark-color, var(--mat-sys-surface));
}
@media (forced-colors: active) {
  .mdc-checkbox--disabled .mdc-checkbox__checkmark, .mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox__checkmark {
    color: GrayText;
  }
}

.mdc-checkbox__checkmark-path {
  transition: stroke-dashoffset 180ms cubic-bezier(0.4, 0, 0.6, 1);
  stroke: currentColor;
  stroke-width: 3.12px;
  stroke-dashoffset: 29.7833385;
  stroke-dasharray: 29.7833385;
}

.mdc-checkbox__mixedmark {
  width: 100%;
  height: 0;
  transform: scaleX(0) rotate(0deg);
  border-width: 1px;
  border-style: solid;
  opacity: 0;
  transition: opacity 90ms cubic-bezier(0.4, 0, 0.6, 1), transform 90ms cubic-bezier(0.4, 0, 0.6, 1);
  border-color: var(--mat-checkbox-selected-checkmark-color, var(--mat-sys-on-primary));
}
@media (forced-colors: active) {
  .mdc-checkbox__mixedmark {
    margin: 0 1px;
  }
}

.mdc-checkbox--disabled .mdc-checkbox__mixedmark, .mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox__mixedmark {
  border-color: var(--mat-checkbox-disabled-selected-checkmark-color, var(--mat-sys-surface));
}
@media (forced-colors: active) {
  .mdc-checkbox--disabled .mdc-checkbox__mixedmark, .mdc-checkbox--disabled.mat-mdc-checkbox-disabled-interactive .mdc-checkbox__mixedmark {
    border-color: GrayText;
  }
}

.mdc-checkbox--anim-unchecked-checked .mdc-checkbox__background,
.mdc-checkbox--anim-unchecked-indeterminate .mdc-checkbox__background,
.mdc-checkbox--anim-checked-unchecked .mdc-checkbox__background,
.mdc-checkbox--anim-indeterminate-unchecked .mdc-checkbox__background {
  animation-duration: 180ms;
  animation-timing-function: linear;
}

.mdc-checkbox--anim-unchecked-checked .mdc-checkbox__checkmark-path {
  animation: mdc-checkbox-unchecked-checked-checkmark-path 180ms linear;
  transition: none;
}

.mdc-checkbox--anim-unchecked-indeterminate .mdc-checkbox__mixedmark {
  animation: mdc-checkbox-unchecked-indeterminate-mixedmark 90ms linear;
  transition: none;
}

.mdc-checkbox--anim-checked-unchecked .mdc-checkbox__checkmark-path {
  animation: mdc-checkbox-checked-unchecked-checkmark-path 90ms linear;
  transition: none;
}

.mdc-checkbox--anim-checked-indeterminate .mdc-checkbox__checkmark {
  animation: mdc-checkbox-checked-indeterminate-checkmark 90ms linear;
  transition: none;
}
.mdc-checkbox--anim-checked-indeterminate .mdc-checkbox__mixedmark {
  animation: mdc-checkbox-checked-indeterminate-mixedmark 90ms linear;
  transition: none;
}

.mdc-checkbox--anim-indeterminate-checked .mdc-checkbox__checkmark {
  animation: mdc-checkbox-indeterminate-checked-checkmark 500ms linear;
  transition: none;
}
.mdc-checkbox--anim-indeterminate-checked .mdc-checkbox__mixedmark {
  animation: mdc-checkbox-indeterminate-checked-mixedmark 500ms linear;
  transition: none;
}

.mdc-checkbox--anim-indeterminate-unchecked .mdc-checkbox__mixedmark {
  animation: mdc-checkbox-indeterminate-unchecked-mixedmark 300ms linear;
  transition: none;
}

.mdc-checkbox__native-control:checked ~ .mdc-checkbox__background,
.mdc-checkbox__native-control:indeterminate ~ .mdc-checkbox__background {
  transition: border-color 90ms cubic-bezier(0, 0, 0.2, 1), background-color 90ms cubic-bezier(0, 0, 0.2, 1);
}
.mdc-checkbox__native-control:checked ~ .mdc-checkbox__background > .mdc-checkbox__checkmark > .mdc-checkbox__checkmark-path,
.mdc-checkbox__native-control:indeterminate ~ .mdc-checkbox__background > .mdc-checkbox__checkmark > .mdc-checkbox__checkmark-path {
  stroke-dashoffset: 0;
}

.mdc-checkbox__native-control:checked ~ .mdc-checkbox__background > .mdc-checkbox__checkmark {
  transition: opacity 180ms cubic-bezier(0, 0, 0.2, 1), transform 180ms cubic-bezier(0, 0, 0.2, 1);
  opacity: 1;
}
.mdc-checkbox__native-control:checked ~ .mdc-checkbox__background > .mdc-checkbox__mixedmark {
  transform: scaleX(1) rotate(-45deg);
}

.mdc-checkbox__native-control:indeterminate ~ .mdc-checkbox__background > .mdc-checkbox__checkmark {
  transform: rotate(45deg);
  opacity: 0;
  transition: opacity 90ms cubic-bezier(0.4, 0, 0.6, 1), transform 90ms cubic-bezier(0.4, 0, 0.6, 1);
}
.mdc-checkbox__native-control:indeterminate ~ .mdc-checkbox__background > .mdc-checkbox__mixedmark {
  transform: scaleX(1) rotate(0deg);
  opacity: 1;
}

@keyframes mdc-checkbox-unchecked-checked-checkmark-path {
  0%, 50% {
    stroke-dashoffset: 29.7833385;
  }
  50% {
    animation-timing-function: cubic-bezier(0, 0, 0.2, 1);
  }
  100% {
    stroke-dashoffset: 0;
  }
}
@keyframes mdc-checkbox-unchecked-indeterminate-mixedmark {
  0%, 68.2% {
    transform: scaleX(0);
  }
  68.2% {
    animation-timing-function: cubic-bezier(0, 0, 0, 1);
  }
  100% {
    transform: scaleX(1);
  }
}
@keyframes mdc-checkbox-checked-unchecked-checkmark-path {
  from {
    animation-timing-function: cubic-bezier(0.4, 0, 1, 1);
    opacity: 1;
    stroke-dashoffset: 0;
  }
  to {
    opacity: 0;
    stroke-dashoffset: -29.7833385;
  }
}
@keyframes mdc-checkbox-checked-indeterminate-checkmark {
  from {
    animation-timing-function: cubic-bezier(0, 0, 0.2, 1);
    transform: rotate(0deg);
    opacity: 1;
  }
  to {
    transform: rotate(45deg);
    opacity: 0;
  }
}
@keyframes mdc-checkbox-indeterminate-checked-checkmark {
  from {
    animation-timing-function: cubic-bezier(0.14, 0, 0, 1);
    transform: rotate(45deg);
    opacity: 0;
  }
  to {
    transform: rotate(360deg);
    opacity: 1;
  }
}
@keyframes mdc-checkbox-checked-indeterminate-mixedmark {
  from {
    animation-timing-function: cubic-bezier(0, 0, 0.2, 1);
    transform: rotate(-45deg);
    opacity: 0;
  }
  to {
    transform: rotate(0deg);
    opacity: 1;
  }
}
@keyframes mdc-checkbox-indeterminate-checked-mixedmark {
  from {
    animation-timing-function: cubic-bezier(0.14, 0, 0, 1);
    transform: rotate(0deg);
    opacity: 1;
  }
  to {
    transform: rotate(315deg);
    opacity: 0;
  }
}
@keyframes mdc-checkbox-indeterminate-unchecked-mixedmark {
  0% {
    animation-timing-function: linear;
    transform: scaleX(1);
    opacity: 1;
  }
  32.8%, 100% {
    transform: scaleX(0);
    opacity: 0;
  }
}
.mat-mdc-checkbox {
  display: inline-block;
  position: relative;
  -webkit-tap-highlight-color: transparent;
}
.mat-mdc-checkbox._mat-animation-noopable > .mat-internal-form-field > .mdc-checkbox > .mat-mdc-checkbox-touch-target,
.mat-mdc-checkbox._mat-animation-noopable > .mat-internal-form-field > .mdc-checkbox > .mdc-checkbox__native-control,
.mat-mdc-checkbox._mat-animation-noopable > .mat-internal-form-field > .mdc-checkbox > .mdc-checkbox__ripple,
.mat-mdc-checkbox._mat-animation-noopable > .mat-internal-form-field > .mdc-checkbox > .mat-mdc-checkbox-ripple::before,
.mat-mdc-checkbox._mat-animation-noopable > .mat-internal-form-field > .mdc-checkbox > .mdc-checkbox__background,
.mat-mdc-checkbox._mat-animation-noopable > .mat-internal-form-field > .mdc-checkbox > .mdc-checkbox__background > .mdc-checkbox__checkmark,
.mat-mdc-checkbox._mat-animation-noopable > .mat-internal-form-field > .mdc-checkbox > .mdc-checkbox__background > .mdc-checkbox__checkmark > .mdc-checkbox__checkmark-path,
.mat-mdc-checkbox._mat-animation-noopable > .mat-internal-form-field > .mdc-checkbox > .mdc-checkbox__background > .mdc-checkbox__mixedmark {
  transition: none !important;
  animation: none !important;
}
.mat-mdc-checkbox label {
  cursor: pointer;
}
.mat-mdc-checkbox .mat-internal-form-field {
  color: var(--mat-checkbox-label-text-color, var(--mat-sys-on-surface));
  font-family: var(--mat-checkbox-label-text-font, var(--mat-sys-body-medium-font));
  line-height: var(--mat-checkbox-label-text-line-height, var(--mat-sys-body-medium-line-height));
  font-size: var(--mat-checkbox-label-text-size, var(--mat-sys-body-medium-size));
  letter-spacing: var(--mat-checkbox-label-text-tracking, var(--mat-sys-body-medium-tracking));
  font-weight: var(--mat-checkbox-label-text-weight, var(--mat-sys-body-medium-weight));
}
.mat-mdc-checkbox.mat-mdc-checkbox-disabled.mat-mdc-checkbox-disabled-interactive {
  pointer-events: auto;
}
.mat-mdc-checkbox.mat-mdc-checkbox-disabled.mat-mdc-checkbox-disabled-interactive input {
  cursor: default;
}
.mat-mdc-checkbox.mat-mdc-checkbox-disabled label {
  cursor: default;
  color: var(--mat-checkbox-disabled-label-color, color-mix(in srgb, var(--mat-sys-on-surface) 38%, transparent));
}
@media (forced-colors: active) {
  .mat-mdc-checkbox.mat-mdc-checkbox-disabled label {
    color: GrayText;
  }
}
.mat-mdc-checkbox label:empty {
  display: none;
}
.mat-mdc-checkbox .mdc-checkbox__ripple {
  opacity: 0;
}

.mat-mdc-checkbox .mat-mdc-checkbox-ripple,
.mdc-checkbox__ripple {
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
}
.mat-mdc-checkbox .mat-mdc-checkbox-ripple:not(:empty),
.mdc-checkbox__ripple:not(:empty) {
  transform: translateZ(0);
}

.mat-mdc-checkbox-ripple .mat-ripple-element {
  opacity: 0.1;
}

.mat-mdc-checkbox-touch-target {
  position: absolute;
  top: 50%;
  left: 50%;
  height: var(--mat-checkbox-touch-target-size, 48px);
  width: var(--mat-checkbox-touch-target-size, 48px);
  transform: translate(-50%, -50%);
  display: var(--mat-checkbox-touch-target-display, block);
}

.mat-mdc-checkbox .mat-mdc-checkbox-ripple::before {
  border-radius: 50%;
}

.mdc-checkbox__native-control:focus-visible ~ .mat-focus-indicator::before {
  content: "";
}
`],encapsulation:2,changeDetection:0})}return o})(),dn=(()=>{class o{static \u0275fac=function(t){return new(t||o)};static \u0275mod=Y({type:o});static \u0275inj=B({imports:[Tt,$]})}return o})();function Wn(o,i){o&1&&(c(0,"mat-icon"),l(1,"check"),a())}function qn(o,i){o&1&&(c(0,"span"),l(1,"1"),a())}function Gn(o,i){o&1&&(c(0,"mat-icon"),l(1,"check"),a())}function Kn(o,i){o&1&&(c(0,"span"),l(1,"2"),a())}function Qn(o,i){if(o&1){let e=xe();K(0),c(1,"h2",24),l(2,"Crea tu cuenta"),a(),c(3,"p",25),l(4,"Selecciona tu rol y completa tu informaci\xF3n"),a(),c(5,"div",26)(6,"button",27),x("click",function(){L(e);let n=y();return z(n.rolSeleccionado="PADRE")}),c(7,"mat-icon",28),l(8,"family_restroom"),a(),c(9,"span"),l(10,"Padre / Tutor"),a()(),c(11,"button",27),x("click",function(){L(e);let n=y();return z(n.rolSeleccionado="DOCENTE")}),c(12,"mat-icon",28),l(13,"school"),a(),c(14,"span"),l(15,"Docente"),a()()(),c(16,"button",29),x("click",function(){L(e);let n=y();return z(n.goStep2())}),l(17," Continuar "),c(18,"mat-icon"),l(19,"arrow_forward"),a()(),c(20,"p",30),l(21," \xBFYa tienes cuenta? "),c(22,"a",15),l(23,"Inicia sesi\xF3n"),a()(),Q()}if(o&2){let e=y();d(6),C("active",e.rolSeleccionado==="PADRE"),d(5),C("active",e.rolSeleccionado==="DOCENTE"),d(5),_("disabled",!e.rolSeleccionado)}}function Un(o,i){o&1&&(c(0,"mat-error"),l(1,"Requerido"),a())}function Zn(o,i){o&1&&(c(0,"mat-error"),l(1,"Requerido"),a())}function $n(o,i){o&1&&(c(0,"mat-error"),l(1,"Correo inv\xE1lido"),a())}function Jn(o,i){o&1&&(c(0,"mat-error"),l(1,"M\xEDn. 8 caracteres"),a())}function eo(o,i){o&1&&(c(0,"mat-error"),l(1," Requiere may\xFAscula y n\xFAmero "),a())}function to(o,i){o&1&&(c(0,"mat-error"),l(1,"Requerido"),a())}function io(o,i){o&1&&(c(0,"mat-error"),l(1," No coinciden "),a())}function no(o,i){if(o&1&&(c(0,"div",53)(1,"div",54)(2,"span",55),l(3),a(),c(4,"span",56),l(5),a()(),c(6,"div",57),b(7,"div",58)(8,"div",58)(9,"div",58)(10,"div",58),a()()),o&2){let e=y(2);d(2),ue("color",e.strengthColor),d(),q(e.strengthLabel),d(2),q(e.strengthHint),d(2),ue("background",e.strengthScore>=1?e.strengthColor:"#E2E8F0"),d(),ue("background",e.strengthScore>=2?e.strengthColor:"#E2E8F0"),d(),ue("background",e.strengthScore>=3?e.strengthColor:"#E2E8F0"),d(),ue("background",e.strengthScore>=4?e.strengthColor:"#E2E8F0")}}function oo(o,i){o&1&&(K(0),c(1,"div",32)(2,"mat-form-field",33)(3,"mat-label"),l(4,"Tel\xE9fono (opcional)"),a(),b(5,"input",59),a(),c(6,"mat-form-field",33)(7,"mat-label"),l(8,"Relaci\xF3n con el ni\xF1o"),a(),c(9,"mat-select",60)(10,"mat-option",61),l(11,"Padre"),a(),c(12,"mat-option",62),l(13,"Madre"),a(),c(14,"mat-option",63),l(15,"Tutor legal"),a(),c(16,"mat-option",64),l(17,"Otro"),a()()()(),Q())}function ro(o,i){o&1&&(K(0),c(1,"div",32)(2,"mat-form-field",33)(3,"mat-label"),l(4,"Instituci\xF3n educativa"),a(),b(5,"input",65),a(),c(6,"mat-form-field",33)(7,"mat-label"),l(8,"Grado / Grupo"),a(),b(9,"input",66),a()(),Q())}function ao(o,i){o&1&&(c(0,"div",67),l(1," Debes aceptar los t\xE9rminos para continuar "),a())}function so(o,i){if(o&1&&(c(0,"div",68)(1,"mat-icon"),l(2,"error_outline"),a(),l(3),a()),o&2){let e=y(2);d(3),ze(" ",e.errorMsg," ")}}function co(o,i){o&1&&b(0,"mat-spinner",69)}function lo(o,i){o&1&&(K(0),l(1,"Crear mi cuenta gratis"),Q())}function ho(o,i){o&1&&(K(0),l(1,"Creando cuenta\u2026"),Q())}function mo(o,i){if(o&1){let e=xe();K(0),c(1,"h2",24),l(2,"Completa tus datos"),a(),c(3,"p",25),l(4,"Ingresa tu informaci\xF3n para crear la cuenta"),a(),c(5,"form",31),x("ngSubmit",function(){L(e);let n=y();return z(n.onSubmit())}),c(6,"div",32)(7,"mat-form-field",33)(8,"mat-label"),l(9,"Nombre"),a(),b(10,"input",34),E(11,Un,2,0,"mat-error",21),a(),c(12,"mat-form-field",33)(13,"mat-label"),l(14,"Apellido"),a(),b(15,"input",35),a()(),c(16,"mat-form-field",36)(17,"mat-label"),l(18,"Correo electr\xF3nico"),a(),b(19,"input",37),c(20,"mat-icon",38),l(21,"mail"),a(),E(22,Zn,2,0,"mat-error",21)(23,$n,2,0,"mat-error",21),a(),c(24,"div",32)(25,"mat-form-field",33)(26,"mat-label"),l(27,"Contrase\xF1a"),a(),b(28,"input",39),c(29,"button",40),x("click",function(){L(e);let n=y();return z(n.hidePassword=!n.hidePassword)}),c(30,"mat-icon"),l(31),a()(),E(32,Jn,2,0,"mat-error",21)(33,eo,2,0,"mat-error",21),a(),c(34,"mat-form-field",33)(35,"mat-label"),l(36,"Confirmar contrase\xF1a"),a(),b(37,"input",41),c(38,"button",40),x("click",function(){L(e);let n=y();return z(n.hideConfirm=!n.hideConfirm)}),c(39,"mat-icon"),l(40),a()(),E(41,to,2,0,"mat-error",21)(42,io,2,0,"mat-error",21),a()(),E(43,no,11,12,"div",42)(44,oo,18,0,"ng-container",21)(45,ro,10,0,"ng-container",21),c(46,"div",43)(47,"mat-checkbox",44),l(48," Acepto los "),c(49,"a",45),l(50,"T\xE9rminos de servicio"),a(),l(51," y la "),c(52,"a",46),l(53,"Pol\xEDtica de privacidad"),a(),l(54," de FocusKids "),a(),E(55,ao,2,0,"div",47),a(),E(56,so,4,1,"div",48),c(57,"div",49)(58,"button",50),x("click",function(){L(e);let n=y();return z(n.step=1)}),c(59,"mat-icon"),l(60,"arrow_back"),a()(),c(61,"button",51),E(62,co,1,0,"mat-spinner",52)(63,lo,2,0,"ng-container",21)(64,ho,2,0,"ng-container",21),a()()(),Q()}if(o&2){let e,t,n,r,s,h,m,g,f,u=y();d(5),_("formGroup",u.form),d(6),_("ngIf",(e=u.form.get("nombre"))==null?null:e.hasError("required")),d(11),_("ngIf",(t=u.form.get("email"))==null?null:t.hasError("required")),d(),_("ngIf",(n=u.form.get("email"))==null?null:n.hasError("email")),d(5),_("type",u.hidePassword?"password":"text"),d(3),q(u.hidePassword?"visibility_off":"visibility"),d(),_("ngIf",(r=u.form.get("password"))==null?null:r.hasError("minlength")),d(),_("ngIf",((s=u.form.get("password"))==null?null:s.hasError("pattern"))&&!((s=u.form.get("password"))!=null&&s.hasError("minlength"))),d(4),_("type",u.hideConfirm?"password":"text"),d(3),q(u.hideConfirm?"visibility_off":"visibility"),d(),_("ngIf",(h=u.form.get("confirmPassword"))==null?null:h.hasError("required")),d(),_("ngIf",(m=u.form.get("confirmPassword"))==null?null:m.hasError("passwordMismatch")),d(),_("ngIf",(g=u.form.get("password"))==null?null:g.value),d(),_("ngIf",u.rolSeleccionado==="PADRE"),d(),_("ngIf",u.rolSeleccionado==="DOCENTE"),d(10),_("ngIf",((f=u.form.get("aceptaTerminos"))==null?null:f.invalid)&&((f=u.form.get("aceptaTerminos"))==null?null:f.touched)),d(),_("ngIf",u.errorMsg),d(5),_("disabled",u.form.invalid||u.loading),d(),_("ngIf",u.loading),d(),_("ngIf",!u.loading),d(),_("ngIf",u.loading)}}function po(o,i){if(o&1&&(K(0),c(1,"div",70)(2,"div",71),l(3,"\u{1F389}"),a(),c(4,"h2"),l(5,"\xA1Cuenta creada!"),a(),c(6,"p"),l(7),a(),c(8,"a",72)(9,"mat-icon"),l(10,"login"),a(),l(11," Iniciar sesi\xF3n "),a()(),Q()),o&2){let e=y();d(7),q(e.successMsg)}}var hn=class o{constructor(i,e,t,n){this.fb=i;this.auth=e;this.router=t;this.cdr=n;this.form=this.fb.group({nombre:["",V.required],apellido:[""],email:["",[V.required,V.email]],password:["",[V.required,V.minLength(8),V.pattern(/^(?=.*[A-Z])(?=.*\d).+$/)]],confirmPassword:["",V.required],rol:["PADRE",V.required],telefono:[""],relacionConNino:[""],institucion:[""],gradoGrupo:[""],aceptaTerminos:[!1,V.requiredTrue]},{validators:this.passwordMatchValidator}),this.form.get("password")?.valueChanges.subscribe(()=>this.actualizarCoincidenciaPassword()),this.form.get("confirmPassword")?.valueChanges.subscribe(()=>this.actualizarCoincidenciaPassword())}form;loading=!1;errorMsg="";successMsg="";hidePassword=!0;hideConfirm=!0;step=1;rolSeleccionado="";passwordMatchValidator(i){let e=i.get("password")?.value,t=i.get("confirmPassword")?.value;return e&&t&&e!==t?{passwordMismatch:!0}:null}actualizarCoincidenciaPassword(){let i=this.form.get("confirmPassword");if(!i)return;let e=this.form.get("password")?.value,t=i.value,s=i.errors||{},{passwordMismatch:n}=s,r=rt(s,["passwordMismatch"]);if(e&&t&&e!==t)i.setErrors(be(te({},r),{passwordMismatch:!0}));else{let h=Object.keys(r).length?r:null;i.setErrors(h)}}goStep2(){this.rolSeleccionado&&(this.form.patchValue({rol:this.rolSeleccionado}),this.step=2)}get strengthScore(){let i=this.form.get("password")?.value||"",e=0;return i.length>=8&&e++,/[A-Z]/.test(i)&&e++,/\d/.test(i)&&e++,/[^A-Za-z0-9]/.test(i)&&e++,e}get strengthLabel(){return["","D\xE9bil","Regular","Buena","Segura"][this.strengthScore]||""}get strengthHint(){let i=this.form.get("password")?.value||"";return i.length<8?"M\xEDn. 8 caracteres":/[A-Z]/.test(i)?/\d/.test(i)?/[^A-Za-z0-9]/.test(i)?"\xA1Contrase\xF1a segura!":"Agrega un s\xEDmbolo (!@#...)":"Agrega un n\xFAmero":"Agrega una may\xFAscula"}get strengthColor(){return["","#EF4444","#F59E0B","#3B82F6","#4F46E5"][this.strengthScore]||"#E2E8F0"}onSubmit(){if(this.form.invalid){this.form.markAllAsTouched();return}this.loading=!0,this.errorMsg="";let h=this.form.value,{confirmPassword:i,aceptaTerminos:e,apellido:t,nombre:n}=h,r=rt(h,["confirmPassword","aceptaTerminos","apellido","nombre"]),s=be(te({},r),{nombre:`${n} ${t}`.trim()});this.auth.register(s).subscribe({next:m=>{this.loading=!1,this.successMsg=m.mensaje||"Cuenta creada exitosamente. Ya puedes iniciar sesi\xF3n.",this.step=3,this.cdr.detectChanges()},error:m=>{if(this.loading=!1,m?.name==="TimeoutError"){this.errorMsg="El servidor est\xE1 tardando demasiado en responder. Puede que tu cuenta s\xED se haya creado \u2014 intenta iniciar sesi\xF3n, o vuelve a intentarlo en un momento.",this.cdr.detectChanges();return}let g=m?.error?.campos;this.errorMsg=m?.error?.error||(g?Object.values(g)[0]:null)||"Error al crear la cuenta. Intente de nuevo.",this.cdr.detectChanges()}})}static \u0275fac=function(e){return new(e||o)(ye(ui),ye(Zt),ye(Qt),ye(Z))};static \u0275cmp=A({type:o,selectors:[["app-register"]],decls:71,vars:21,consts:[[1,"reg-page"],[1,"reg-left"],[1,"orb","o1"],[1,"orb","o2"],[1,"orb","o3"],[1,"orb","o4"],[1,"orb","o5"],[1,"left-inner"],[1,"brand"],[1,"brand-ico"],[1,"brand-name"],[1,"left-pitch"],[1,"features"],[1,"feat-ico"],[1,"left-login"],["routerLink","/auth/login"],[1,"reg-right"],[1,"form-wrap"],[1,"steps"],[1,"step"],[1,"step-circle"],[4,"ngIf"],[1,"step-label"],[1,"step-line"],[1,"form-title"],[1,"form-sub"],[1,"role-cards"],["type","button",1,"role-card",3,"click"],[1,"role-mat-icon"],[1,"btn-primary","full-width",3,"click","disabled"],[1,"bottom-link"],[3,"ngSubmit","formGroup"],[1,"field-row"],["appearance","outline",1,"field"],["matInput","","formControlName","nombre","placeholder","Tu nombre"],["matInput","","formControlName","apellido","placeholder","Tu apellido"],["appearance","outline",1,"full-width"],["matInput","","formControlName","email","type","email","placeholder","ejemplo@correo.com"],["matSuffix",""],["matInput","","formControlName","password","placeholder","M\xEDn. 8 caracteres",3,"type"],["mat-icon-button","","matSuffix","","type","button",3,"click"],["matInput","","formControlName","confirmPassword","placeholder","Repite la contrase\xF1a",3,"type"],["class","strength-wrap",4,"ngIf"],[1,"terms-row"],["formControlName","aceptaTerminos","color","primary"],["routerLink","/terminos","target","_blank"],["routerLink","/privacidad","target","_blank"],["class","terms-error",4,"ngIf"],["class","error-msg",4,"ngIf"],[1,"btn-row"],["type","button",1,"btn-ghost",3,"click"],["type","submit",1,"btn-primary",3,"disabled"],["diameter","18","style","margin-right:8px",4,"ngIf"],[1,"strength-wrap"],[1,"strength-row"],[1,"strength-lbl"],[1,"strength-hint"],[1,"strength-segs"],[1,"seg"],["matInput","","formControlName","telefono","placeholder","88881234"],["formControlName","relacionConNino"],["value","Padre"],["value","Madre"],["value","Tutor"],["value","Otro"],["matInput","","formControlName","institucion","placeholder","Nombre del centro"],["matInput","","formControlName","gradoGrupo","placeholder","Ej: 3\xB0 A"],[1,"terms-error"],[1,"error-msg"],["diameter","18",2,"margin-right","8px"],[1,"success-state"],[1,"success-ico"],["routerLink","/auth/login",1,"btn-primary",2,"text-decoration","none","display","inline-flex","align-items","center","gap","6px"]],template:function(e,t){e&1&&(c(0,"div",0)(1,"div",1),b(2,"div",2)(3,"div",3)(4,"div",4)(5,"div",5)(6,"div",6),c(7,"div",7)(8,"div",8)(9,"span",9),l(10,"\u{1F9E0}"),a(),c(11,"span",10),l(12,"FocusKids"),a()(),c(13,"div",11)(14,"h2"),l(15,"Crea tu cuenta"),b(16,"br"),l(17,"en minutos"),a(),c(18,"p"),l(19,"\xDAnete a las familias que ya est\xE1n transformando el aprendizaje de sus hijos con TDAH."),a()(),c(20,"ul",12)(21,"li")(22,"span",13)(23,"mat-icon"),l(24,"videogame_asset"),a()(),l(25," 12 juegos cognitivos adaptativos "),a(),c(26,"li")(27,"span",13)(28,"mat-icon"),l(29,"insights"),a()(),l(30," Seguimiento de progreso en tiempo real "),a(),c(31,"li")(32,"span",13)(33,"mat-icon"),l(34,"emoji_events"),a()(),l(35," Sistema de logros y recompensas "),a(),c(36,"li")(37,"span",13)(38,"mat-icon"),l(39,"lock"),a()(),l(40," Privacidad y seguridad garantizadas "),a()(),c(41,"p",14),l(42," \xBFYa tienes cuenta? "),c(43,"a",15),l(44,"Inicia sesi\xF3n aqu\xED"),a()()()(),c(45,"div",16)(46,"div",17)(47,"div",18)(48,"div",19)(49,"div",20),E(50,Wn,2,0,"mat-icon",21)(51,qn,2,0,"span",21),a(),c(52,"span",22),l(53,"Rol"),a()(),b(54,"div",23),c(55,"div",19)(56,"div",20),E(57,Gn,2,0,"mat-icon",21)(58,Kn,2,0,"span",21),a(),c(59,"span",22),l(60,"Datos"),a()(),b(61,"div",23),c(62,"div",19)(63,"div",20)(64,"span"),l(65,"3"),a()(),c(66,"span",22),l(67,"Listo"),a()()(),E(68,Qn,24,5,"ng-container",21)(69,mo,65,21,"ng-container",21)(70,po,12,1,"ng-container",21),a()()()),e&2&&(d(48),C("done",t.step>1)("active",t.step===1),d(2),_("ngIf",t.step>1),d(),_("ngIf",t.step<=1),d(3),C("done",t.step>1),d(),C("done",t.step>2)("active",t.step===2),d(2),_("ngIf",t.step>2),d(),_("ngIf",t.step<=2),d(3),C("done",t.step>2),d(),C("active",t.step===3),d(6),_("ngIf",t.step===1),d(),_("ngIf",t.step===2),d(),_("ngIf",t.step===3))},dependencies:[fi,mi,ai,li,di,qe,pi,Ut,Kt,Ge,Mi,Ci,wi,Oi,Ii,Di,cn,sn,ge,xi,ki,yi,vi,Ti,Ai,dn,Tt],styles:['@charset "UTF-8";[_nghost-%COMP%]{display:block}.reg-page[_ngcontent-%COMP%]{display:flex;min-height:100vh}.reg-left[_ngcontent-%COMP%]{width:380px;min-width:320px;flex-shrink:0;background:linear-gradient(160deg,#0d0620,#1e1b4b 55%,#2d1272);padding:0 44px;display:flex;align-items:center;position:relative;overflow:hidden}.orb[_ngcontent-%COMP%]{position:absolute;border-radius:50%;pointer-events:none;will-change:transform,opacity}.o1[_ngcontent-%COMP%]{width:280px;height:280px;top:-80px;right:-80px;background:radial-gradient(circle at 35% 35%,rgba(129,140,248,.55) 0%,rgba(79,70,229,.3) 45%,transparent 70%);animation:_ngcontent-%COMP%_float1 9s ease-in-out infinite}.o2[_ngcontent-%COMP%]{width:200px;height:200px;bottom:-60px;left:-60px;background:radial-gradient(circle at 40% 40%,rgba(167,139,250,.5) 0%,rgba(124,58,237,.25) 50%,transparent 70%);animation:_ngcontent-%COMP%_float2 11s ease-in-out infinite}.o3[_ngcontent-%COMP%]{width:110px;height:110px;top:42%;left:10%;background:radial-gradient(circle at 40% 35%,rgba(196,181,253,.45) 0%,rgba(139,92,246,.2) 55%,transparent 70%);animation:_ngcontent-%COMP%_float3 7s ease-in-out infinite}.o4[_ngcontent-%COMP%]{width:80px;height:80px;top:18%;left:18%;background:radial-gradient(circle at 38% 38%,rgba(165,180,252,.5) 0%,rgba(99,102,241,.22) 55%,transparent 70%);animation:_ngcontent-%COMP%_float4 8s ease-in-out infinite}.o5[_ngcontent-%COMP%]{width:55px;height:55px;bottom:28%;right:14%;background:radial-gradient(circle at 35% 35%,rgba(224,214,255,.6) 0%,rgba(139,92,246,.25) 55%,transparent 70%);animation:_ngcontent-%COMP%_float5 6s ease-in-out infinite}@keyframes _ngcontent-%COMP%_float1{0%,to{transform:translate(0) scale(1)}33%{transform:translate(-18px,22px) scale(1.04)}66%{transform:translate(12px,-14px) scale(.97)}}@keyframes _ngcontent-%COMP%_float2{0%,to{transform:translate(0) scale(1)}40%{transform:translate(20px,-24px) scale(1.06)}70%{transform:translate(-10px,10px) scale(.96)}}@keyframes _ngcontent-%COMP%_float3{0%,to{transform:translate(0) scale(1);opacity:.9}50%{transform:translate(14px,-18px) scale(1.08);opacity:1}}@keyframes _ngcontent-%COMP%_float4{0%,to{transform:translate(0);opacity:.85}45%{transform:translate(-12px,16px);opacity:1}80%{transform:translate(8px,-8px);opacity:.75}}@keyframes _ngcontent-%COMP%_float5{0%,to{transform:translate(0) scale(1);opacity:.8}50%{transform:translate(-10px,-14px) scale(1.12);opacity:1}}.left-inner[_ngcontent-%COMP%]{position:relative;z-index:1;width:100%}.brand[_ngcontent-%COMP%]{display:flex;align-items:center;gap:10px;margin-bottom:44px}.brand-ico[_ngcontent-%COMP%]{font-size:28px}.brand-name[_ngcontent-%COMP%]{font-family:"Baloo 2",Quicksand,sans-serif;font-size:20px;font-weight:800;color:#fff}.left-pitch[_ngcontent-%COMP%]   h2[_ngcontent-%COMP%]{font-family:"Baloo 2",Quicksand,sans-serif;font-size:30px;font-weight:800;color:#fff;line-height:1.2;margin:0 0 14px}.left-pitch[_ngcontent-%COMP%]   p[_ngcontent-%COMP%]{font-family:Quicksand,sans-serif;font-size:14px;color:#fff9;line-height:1.65;margin:0 0 36px}.features[_ngcontent-%COMP%]{list-style:none;margin:0 0 40px;padding:0;display:flex;flex-direction:column;gap:16px}.features[_ngcontent-%COMP%]   li[_ngcontent-%COMP%]{display:flex;align-items:center;gap:14px;font-family:Quicksand,sans-serif;font-size:14px;color:#ffffffd9;font-weight:600}.feat-ico[_ngcontent-%COMP%]{width:38px;height:38px;border-radius:11px;flex-shrink:0;background:#818cf82e;border:1px solid rgba(129,140,248,.2);display:flex;align-items:center;justify-content:center}.feat-ico[_ngcontent-%COMP%]   mat-icon[_ngcontent-%COMP%]{color:#818cf8;font-size:18px;width:18px;height:18px}.left-login[_ngcontent-%COMP%]{font-family:Quicksand,sans-serif;font-size:13.5px;color:#ffffff80;margin:0}.left-login[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]{color:#a5b4fc;font-weight:700;text-decoration:none}.left-login[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:hover{color:#c7d2fe;text-decoration:underline}.reg-right[_ngcontent-%COMP%]{flex:1;background:#f8f7ff;display:flex;align-items:center;justify-content:center;padding:40px 24px}.form-wrap[_ngcontent-%COMP%]{width:100%;max-width:560px;background:#fff;border-radius:24px;box-shadow:0 8px 40px #4f46e51a;padding:36px 40px}.steps[_ngcontent-%COMP%]{display:flex;align-items:center;justify-content:center;margin-bottom:32px}.step[_ngcontent-%COMP%]{display:flex;flex-direction:column;align-items:center;gap:6px}.step-circle[_ngcontent-%COMP%]{width:38px;height:38px;border-radius:50%;border:2px solid #E2E8F0;background:#fff;display:flex;align-items:center;justify-content:center;font-family:Quicksand,sans-serif;font-size:14px;font-weight:700;color:#94a3b8;transition:all .3s}.step.active[_ngcontent-%COMP%]   .step-circle[_ngcontent-%COMP%]{border-color:#4f46e5;background:#4f46e5;color:#fff;box-shadow:0 0 0 4px #4f46e526}.step.done[_ngcontent-%COMP%]   .step-circle[_ngcontent-%COMP%]{border-color:#4f46e5;background:#4f46e5;color:#fff}.step.done[_ngcontent-%COMP%]   .step-circle[_ngcontent-%COMP%]   mat-icon[_ngcontent-%COMP%]{font-size:16px;width:16px;height:16px}.step-label[_ngcontent-%COMP%]{font-family:Quicksand,sans-serif;font-size:11px;font-weight:600;color:#94a3b8;transition:color .3s}.step.active[_ngcontent-%COMP%]   .step-label[_ngcontent-%COMP%], .step.done[_ngcontent-%COMP%]   .step-label[_ngcontent-%COMP%]{color:#4f46e5}.step-line[_ngcontent-%COMP%]{flex:1;height:2px;background:#e2e8f0;margin:0 10px 18px;min-width:40px;transition:background .3s}.step-line.done[_ngcontent-%COMP%]{background:#4f46e5}.form-title[_ngcontent-%COMP%]{font-family:"Baloo 2",Quicksand,sans-serif;font-size:22px;font-weight:800;color:#1e1b4b;margin:0 0 4px}.form-sub[_ngcontent-%COMP%]{font-family:Quicksand,sans-serif;font-size:13.5px;color:#94a3b8;margin:0 0 24px}.role-cards[_ngcontent-%COMP%]{display:grid;grid-template-columns:1fr 1fr;gap:14px;margin-bottom:24px}.role-card[_ngcontent-%COMP%]{display:flex;flex-direction:column;align-items:center;gap:12px;padding:28px 16px;border:2px solid #E2E8F0;border-radius:18px;background:#fff;cursor:pointer;font-family:Quicksand,sans-serif;font-size:14px;font-weight:700;color:#64748b;transition:all .2s}.role-card[_ngcontent-%COMP%]:hover{border-color:#818cf8;color:#4f46e5;background:#f5f3ff}.role-card.active[_ngcontent-%COMP%]{border-color:#4f46e5;background:linear-gradient(135deg,#f5f3ff,#ede9fe);color:#4f46e5;box-shadow:0 4px 20px #4f46e52e}.role-mat-icon[_ngcontent-%COMP%]{font-size:38px;width:38px;height:38px}.field-row[_ngcontent-%COMP%]{display:grid;grid-template-columns:1fr 1fr;gap:12px}.field[_ngcontent-%COMP%], .full-width[_ngcontent-%COMP%]{width:100%}  .reg-right .mat-mdc-form-field.mat-focused .mdc-notched-outline__leading,   .reg-right .mat-mdc-form-field.mat-focused .mdc-notched-outline__notch,   .reg-right .mat-mdc-form-field.mat-focused .mdc-notched-outline__trailing{border-color:#4f46e5!important}  .reg-right .mat-mdc-form-field.mat-focused .mat-mdc-floating-label{color:#4f46e5!important}  .reg-right .mat-mdc-checkbox.mat-primary .mdc-checkbox__background{border-color:#4f46e5!important}.strength-wrap[_ngcontent-%COMP%]{margin:-2px 0 12px}.strength-row[_ngcontent-%COMP%]{display:flex;justify-content:space-between;margin-bottom:6px}.strength-lbl[_ngcontent-%COMP%]{font-family:Quicksand,sans-serif;font-size:12px;font-weight:700}.strength-hint[_ngcontent-%COMP%]{font-family:Quicksand,sans-serif;font-size:11.5px;color:#94a3b8}.strength-segs[_ngcontent-%COMP%]{display:flex;gap:5px}.seg[_ngcontent-%COMP%]{flex:1;height:5px;border-radius:3px;transition:background .3s}.terms-row[_ngcontent-%COMP%]{margin:8px 0 14px}.terms-row[_ngcontent-%COMP%]   mat-checkbox[_ngcontent-%COMP%]{font-family:Quicksand,sans-serif;font-size:13px;color:#64748b}.terms-row[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]{color:#4f46e5;font-weight:700;text-decoration:none}.terms-row[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:hover{text-decoration:underline}.terms-error[_ngcontent-%COMP%]{font-family:Quicksand,sans-serif;font-size:11.5px;color:#e8607a;margin-top:4px;padding-left:4px}.error-msg[_ngcontent-%COMP%]{display:flex;align-items:center;gap:8px;background:#fef2f2;color:#dc2626;font-family:Quicksand,sans-serif;font-weight:600;font-size:13px;padding:10px 12px;border-radius:12px;margin-bottom:12px}.btn-row[_ngcontent-%COMP%]{display:flex;gap:10px;align-items:center}.btn-primary[_ngcontent-%COMP%]{flex:1;display:flex;align-items:center;justify-content:center;gap:6px;padding:14px 24px;background:linear-gradient(135deg,#4f46e5,#7c3aed);color:#fff;border:none;border-radius:14px;font-family:"Baloo 2",Quicksand,sans-serif;font-size:15px;font-weight:700;cursor:pointer;transition:all .2s;box-sizing:border-box}.btn-primary[_ngcontent-%COMP%]:hover:not([disabled]){box-shadow:0 6px 24px #4f46e566;transform:translateY(-1px)}.btn-primary[disabled][_ngcontent-%COMP%]{opacity:.5;cursor:not-allowed}.btn-primary.full-width[_ngcontent-%COMP%]{width:100%;margin-top:4px}.btn-ghost[_ngcontent-%COMP%]{display:flex;align-items:center;justify-content:center;width:48px;height:48px;flex-shrink:0;background:#f1f5f9;color:#64748b;border:none;border-radius:14px;cursor:pointer;transition:all .2s}.btn-ghost[_ngcontent-%COMP%]:hover{background:#e2e8f0;color:#1e293b}.bottom-link[_ngcontent-%COMP%]{text-align:center;margin-top:16px;font-family:Quicksand,sans-serif;font-size:13.5px;color:#94a3b8}.bottom-link[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]{color:#4f46e5;font-weight:700;text-decoration:none}.bottom-link[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:hover{text-decoration:underline}.success-state[_ngcontent-%COMP%]{text-align:center;padding:20px 0}.success-ico[_ngcontent-%COMP%]{font-size:60px;margin-bottom:16px}.success-state[_ngcontent-%COMP%]   h2[_ngcontent-%COMP%]{font-family:"Baloo 2",Quicksand,sans-serif;font-size:24px;font-weight:800;color:#4f46e5;margin:0 0 10px}.success-state[_ngcontent-%COMP%]   p[_ngcontent-%COMP%]{font-family:Quicksand,sans-serif;font-size:14.5px;color:#64748b;margin:0 0 28px;line-height:1.6}@media(max-width:900px){.reg-left[_ngcontent-%COMP%]{display:none}.reg-right[_ngcontent-%COMP%]{background:linear-gradient(160deg,#0d0620,#1e1b4b 55%,#2d1272)}.form-wrap[_ngcontent-%COMP%]{box-shadow:0 8px 40px #0000004d}}@media(max-width:540px){.form-wrap[_ngcontent-%COMP%]{padding:28px 20px}.field-row[_ngcontent-%COMP%]{grid-template-columns:1fr}.role-cards[_ngcontent-%COMP%]{grid-template-columns:1fr 1fr}}']})};export{hn as RegisterComponent};
