package com.github.mygreen.supercsv.cellprocessor;

import java.lang.annotation.Annotation;
import java.util.Optional;

import org.supercsv.cellprocessor.ift.CellProcessor;

import com.github.mygreen.supercsv.builder.Configuration;
import com.github.mygreen.supercsv.builder.FieldAccessor;
import com.github.mygreen.supercsv.cellprocessor.format.TextFormatter;

/**
 * 制約のアノテーションを元に値の検証を行う{@link CellProcessor}を作成するためのインタフェース。
 * 
 * @param <A> 対応する制約のアノテーション。
 * @since 2.0
 * @author T.TSUCHIE
 *
 */
@FunctionalInterface
public interface ConstraintProcessorFactory<A extends Annotation> {
    
    /**
     * 値を検証する{@link CellProcessor}を作成します。
     * 
     * @param anno ハンドリング対象のアノテーションです。
     * @param next chainで次に実行する{@link CellProcessor}。値がない場合があります。
     * @param field 処理対象のフィールド情報。
     * @param formatter フィールドの書式に沿ったフォーマッタ。
     * @param config システム情報設定。
     * @return {@link CellProcessor}の実装クラスのインスタンス。
     *         引数nextをそのまま返す場合、値がない場合がある。
     */
    Optional<CellProcessor> create(A anno, Optional<CellProcessor> next, FieldAccessor field,
            TextFormatter<?> formatter, Configuration config);
    
}
